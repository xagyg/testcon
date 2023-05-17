// Author: Brad Long
// Last update: 26 Sep 2001

package conan;

import java.io.*; 
import java.util.*;
 
public class Conan {    
    
    private List objList, cmdList, uniqList, lineList;
    public Set testSet;
    public Map condMap;
    public Map intMap, charMap, doubleMap, floatMap, byteMap, StringMap, longMap, shortMap;
    public Map intArrayMap, charArrayMap, doubleArrayMap, floatArrayMap, byteArrayMap, StringArrayMap, longArrayMap, shortArrayMap;
    public Map monitorMap, optionMap;
    public List testCase, setup, setupVals;
    private String monitor, var, driver, constructor = "";
    private String strToken, endCommand = null, beginCommand = "",
        topCommand = "";
    private String global = "", globalval = "shared"; 
    private int ticktime = 500;
    private String option = "";
    private String caseStr = "";
    
    private int deadlock = 0;
    private int thread_count = 0;
    
    private static int syntaxError = 0;
    
    public int cases = 0;  
    private int line = 0;
    
    private PrintWriter out;
    
    private final static String CONTROL = "#";
    
    public Conan(String driver) throws FileNotFoundException {
        objList = new ArrayList();
        cmdList = new ArrayList();
        uniqList = new ArrayList();                  
        lineList = new ArrayList();   
        condMap = new HashMap();
        monitorMap = new HashMap();
        testSet = new HashSet();        
        intMap = new HashMap();
        charMap = new HashMap();
        doubleMap = new HashMap();
        floatMap = new HashMap();
        byteMap = new HashMap();
        longMap = new HashMap();
        shortMap = new HashMap();
        StringMap = new HashMap();
        intArrayMap = new HashMap();
        charArrayMap = new HashMap();
        doubleArrayMap = new HashMap();
        floatArrayMap = new HashMap();
        byteArrayMap = new HashMap();
        longArrayMap = new HashMap();
        shortArrayMap = new HashMap();
        StringArrayMap = new HashMap();       
        optionMap = new HashMap();   
        monitor = "";              
        testCase = new ArrayList(); 
        setup = new ArrayList();
        setupVals = new ArrayList();
        out = new PrintWriter(new FileOutputStream(driver+".java"));        
    }
    
    public static String ls;
    
    public static void main(String[] args) {
                                  
        Conan.ls = System.getProperty("line.separator");
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer st;            
        String token, thread = "";
        boolean tick = false, begin = false, firstCase = true, atick = false;      
        String lastcommand = "", command = ""; 
        int numends = 0, seq = 0;             
        boolean tickmultiplier = false;
        String driver = (args.length == 0 ? "MonitorDriver" : args[0]);     
        Conan b = null;
        int option_savepoint = 0;
        
        try {
            
            b = new Conan(driver);  
            
            // get monitor name from command line (if provided)
            b.monitor = (args.length < 2 ? "" : args[1]); 
            
            st = new StreamTokenizer(in);
            
            // initialize chars for the tokenizer                        
            initializeChars(st);            
            
            // get first token
            token = b.getToken(st); 
            if (!b.isSpecialChar()) {
                syntax("invalid command: " + token);
                token = null;
            }
            
            outer:        
            while (token != null) {   
                
                tickmultiplier = false;

                {        
                  String str; 
                  while (!b.isSpecialChar()) {
                    str = b.getToken(st);
                    if (!b.isSpecialChar()) {
                      if (str.equals("*") && lastcommand.equals("tick")) {
                        tickmultiplier = true;
                        break;
                      }      
                      syntax("invalid command: " + str);
                      if (begin && !tick)
                        System.err.println("Are you missing a #shared block?");
                      break outer;
                    }
                  }
                }
 
                //out.println(st.toString());
                command = b.getToken(st);
                
                if (command == null)
                    break;
                    
                if (command.equals("begin")) {              
                    ++seq;
                    System.err.println("Sequence: " + seq);
                }
                
                System.err.println("  command: " + command
                    + (tickmultiplier?" (tick multiplier)":""));
                
                /*if (command.equals("driver")) {
                    driver = b.parseDriver(st);  
                } else */              
                
                if (command.equals("monitor") || command.equals("cut")) {
                    if (!b.monitor.equals("")) {
                        warn("Command line monitor overrides monitor specified in ConAn script");
                    }
                    b.parseMonitor(st, seq);            
                }
                            
                else if (command.equals("tick")) {
                    st.ordinaryChar('*');                       
                    if (!begin) {
                        syntax("#begin missing");
                        break;
                    }
                    tick = true;
                    b.option = "";
                    if (!atick) atick = true;
                    numends++; 
                    if (numends != 2) {
                        syntax("#end missing");
                        break;
                    }                    
                    b.line++;
                    //b.parseLine(st, true);
                }
                
                else if (tickmultiplier) {                      
                    numends--; 
                    try {
                        int count = Integer.parseInt(command);
                        b.line = b.line + count - 1;
                    } catch (NumberFormatException e) {
                        syntax("invalid number of ticks");
                        break;
                    }    
                    tick = false;
                    //b.parseLine(st, true);
                }
                                                 
                else if (command.equals("ticktime")) {
                    b.parseTickTime(st);    
                }
                           
                else if (command.endsWith("List")) {
                    if (!begin || tick) {
                        syntax("invalid placement of List command");
                        break;
                    }
                    
                    String listtype = command.substring(0,command.indexOf("List"));
                    
                    if (",int,char,byte,float,double,long,short,String,".indexOf(","+listtype+",") < 0) {
                        syntax("invalid list type");
                        break;
                    }                                             
                    if (b.parseList(listtype, st) == null)
                        break;
                }
                
                else if (command.endsWith("Array")) {
                    if (!begin || tick) {
                        syntax("invalid placement of Array command");
                        break;
                    }
                    
                    String arraytype = command.substring(0,command.indexOf("Array"));
                    
                    if (",int,char,byte,float,double,long,short,String,".indexOf(","+arraytype+",") < 0) {
                        syntax("invalid array type");
                        break;
                    }                                             
                    if (b.parseArray(arraytype, st) == null)
                        break;                    
                }
                
                else if (command.equals("option")) {
                    if (!tick) {
                        syntax("#tick missing");
                        break;
                    }
                    option_savepoint = numends;
                    ++numends;
                    if (numends < 3) {
                        syntax("#end missing");
                        break;
                    } 
                    b.option = b.parseOption(st);                                     
                }
                
                else if (command.equals("thread")) { 
                    if (!tick) {
                        syntax("#tick missing");
                        break;
                    }
                    ++numends;
                    if (numends < 3) {  // was != 3 prior to "option" command
                        syntax("#end missing");
                        break;
                    }   
                    thread = b.parseLine(st,b.option);
                    if (thread == null)
                        break;
                }             
/*********
                else if (command.equals("global")) {
                    if (!firstCase) {
                        syntax("#global must be defined before the first sequence");
                    } 
                    // the above test should catch all issues.
                    // this one not needed.                   
                    if (begin) {
                        syntax("invalid placement of #global block");
                        break;
                    }                                     
                    ++numends;
                    if (!b.parseGlobal(st))
                        break;
                }         
*********/
                else if (command.equals("top")) {
                    ++numends;
                    b.parseTop(st);
                }
                                     
                else if (command.equals("conditions")) {
                    if (begin) {
                        syntax("invalid placement of #conditions block");
                        break;
                    }
                    ++numends;
                    b.parseConditions(st);
                }
                                       
                else if (command.equals("test")) {
                    if (!begin || tick) {
                        syntax("invalid placement of #test block");
                        break;
                    }
                    b.parseTestConditions(st);
                }
                             
                else if (command.equals("selectOption")) {
                    if (!begin || tick) {
                        syntax("invalid placement of #selectOption");
                        break;
                    }
                    b.parseSetOption(st);
                }
                                  
                else if (command.equals("deadlock")) {
                    b.parseDeadlock();                
                }
                                 
                else if (command.equals("begin")) {     
                    begin = true;
                    b.option = "";
                    b.thread_count = 0; 
                    b.setupVals.add(b.globalval);
                    b.setup.add("class ConanSetup"+seq+" extends ConanGlobalSetup {}");
                    ++numends;  
                    if (numends != 1) {
                        syntax("#end missing");
                        break;
                    }                    
                    if (firstCase) {
                        b.buildDefinition(driver);                        
                        firstCase = false;
                    }    
                }
                
                else if (command.equals("shared") || command.equals("fixture")) {
                    if (firstCase && numends == 0) {
                        ++numends;
                        if (!b.parseGlobal(st))
                            break;
                    }
                    else {     
                        if (!begin || tick) {
                            syntax("invalid placement of #shared block");
                            break;
                        }
                        ++numends;                     
                        if (numends != 2) {
                            syntax("#end missing");
                            break;
                        }
                        if (!b.parseSetup(st, seq))
                            break;
                    }
                }
/**
                else if (command.equals("teardown")) {
                    if (!begin || tick) {
                        syntax("invalid placement of #teardown block");
                        break;
                    }
                    ++numends;                     
                    if (numends != 2) {
                        syntax("#end missing");
                        break;
                    }
                    b.parseTeardown(st);                                      
                }                              
**/
                else if (command.equals("end")) {
                    --numends;                        
                    if (numends == 1) {
                        st.wordChars('*','*');
                        tick = false;
                    }
                    
                    if (numends == option_savepoint && option_savepoint > 0) {
                        option_savepoint = 0;
                        b.option = "";
                    }

                    if (numends > 0)
                        continue;
                     
                    if (atick) {
                        atick = false;
                        b.cases++;
                        b.buildScript();
                    }
                    b.clear();         
                }
                  
                else if (command.equals("exit")) {
                    break;
                }
                                            
                else { 
                    syntax(command);
                    break;
                }
                lastcommand = command;   
            }  
            
            if (syntaxError == 0) {
                b.finish();
            }
            else {
                System.err.println("Syntax Errors: " + syntaxError);
                b.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (b!=null) b.close();
        }
    }       
    
    private static void initializeChars(StreamTokenizer st) {
            st.ordinaryChars('0','9');
            st.ordinaryChars('/','/');
            st.ordinaryChar('\'');
            st.ordinaryChar('"');
            st.quoteChar('\'');
            st.quoteChar('"');        
            if (!CONTROL.equals("!"))
                st.wordChars('!','!');
            if (!CONTROL.equals("$"))
                st.wordChars('$','$');
            if (!CONTROL.equals("#"))
                st.wordChars('#','#');
            st.wordChars('%','&');
            st.wordChars('(','.');
            st.wordChars('0','~');     
//            st.ordinaryChar('*');
            //st.wordChars('0','9'); 
            st.eolIsSignificant(false); 
            st.slashSlashComments(true);
            st.slashStarComments(true);
    }                            
    
    private static void syntax (String str) {
        syntaxError++;
        System.err.println("Syntax error: " + str);
    }                         
    
    private static void warn(String str) {
        System.err.println("Warning: " + str);
    }
    
    private void close() {
        out.close();
    }
                                   
    public void clear() {
        objList.clear();
        cmdList.clear();
        uniqList.clear();  
        lineList.clear();
        endCommand = "";            
        beginCommand = "";
//        topCommand = "";
        deadlock = 0;
        line = 0;
    }     
   
    private void buildArray(Map map, String type) {
        Set vars = map.keySet();
        String wvar = "";
        for (Iterator i = vars.iterator(); i.hasNext();) {
            wvar = (String)i.next();
            List l = (List)map.get(wvar);         
            caseStr += "    private static " + type + "[] " + wvar + " = new " + type + "[] {" + l.get(0);
            for (int j=1;j<l.size();++j) {
                caseStr += "," + l.get(j);
            }
            caseStr += "};" + ls;
        }
    }           
    
    private void buildVectorDefinition(Map map) {
        Set vars = map.keySet();
        String wvar = "";
        for (Iterator i = vars.iterator(); i.hasNext();) {
            wvar = (String)i.next();
            List l = (List)map.get(wvar);         
            caseStr += "    private static ConanVector " + wvar + " = new ConanVector();" + ls;
        }
    }       
    
    private void buildVectorAssignment(Map map, String type) {
        Set vars = map.keySet();
        String wvar = "";
        for (Iterator i = vars.iterator(); i.hasNext();) {
            wvar = (String)i.next();
            List l = (List)map.get(wvar);         
           
            for (int j=0;j<l.size();++j) {
             caseStr += "        " + wvar + ".add(("+type+")" + l.get(j) + ");"+ls;
            }
            caseStr += ls;
        }
    }                          
    
    private void buildScript() {
                                           
        String name;
                          
        out.println("    public static void sequence" + cases + "() {" + ls + ls);
        
        Vector v = (Vector)monitorMap.get(new Integer(cases));
        
        if (v == null)
            v = (Vector)monitorMap.get(new Integer(0));
            
        String var = "", monitor = "", constructor = "";
        
        if (v != null) {
            var = (String)v.elementAt(0);
            monitor = (String)v.elementAt(1);
            constructor = (String)v.elementAt(2);        
        }
        else {
            syntax("no monitor defined for test sequence " + cases);
//            return;
        }
        
        //if (v != null) {
        out.println("        " + monitor + " " + var + " = new " + monitor
             + constructor + ";" + ls);
		//}     
		
		out.println("        MonitorClock clock = new MonitorClock();" +ls+
		  "        MonitorTimer timer = new MonitorTimer(clock, " + 
		                  line + ", " + ticktime + ");" + ls +
		  "        ConanThread.clear();" +ls+
		  "        ConanSetup" + cases + " shared = new ConanSetup" 
		                                + cases + "();"+ls+
		  "        shared.setup();" + ls + ls);
        
        for (int i=0; i<uniqList.size(); ++i) {
            name = (String)uniqList.get(i); //(String)iter.next();
            out.println("        ConanThread" /*+ cases*/ + " " + name + 
                                " = new ConanThread" + cases + " (" 
                                + (monitor.equals("")?"":var+",") +
                                "clock," + i + "," + "\""+name+"\", shared);");        
            out.println("        " + name + ".start();");
            out.println("        ConanThread.add(\"" + name + "\"," + name + ");");
        }
        out.println("        timer.start();" + ls   
                  + "        try {" + ls
                  + "            timer.join();");

        out.println("        } catch (InterruptedException e) {}");
        
        if (endCommand != null)
            out.println(endCommand);
            
        out.print("        ConanThread[] threads = {");
        for (int i=0; i<uniqList.size(); ++i) {                   
            name = (String)uniqList.get(i); //(String)iter.next();
            out.print(name);
            if (i == uniqList.size()-1)
                out.println("};");
            else
                out.print(",");
        }                 
        
        out.println("        boolean deadlocked = ConanHelper.checkDeadlock(threads" 
                + "," + deadlock + "," + cases + ",clock.lastTick());");
                
        out.println("        if (!deadlocked) ConanHelper.checkExpectedDeadlock(" 
                    + deadlock + "," + cases + ");");
                    
        out.println("        shared.teardown();");
                                          
        out.println("    }"+ls);
        
        caseStr = "class ConanThread"+cases+" extends ConanThread {" 
                            + ls + ls; 
                            
        caseStr += "    ConanSetup" + cases + " " + setupVals.get(cases-1) 
                            + ";" + ls;
                            
        if (!monitor.equals("")) {
            caseStr += "    " + monitor + " " + var + ";" + ls;
        }         
        
        buildArray(intArrayMap,"int");
        buildArray(longArrayMap,"long");
        buildArray(shortArrayMap,"short");
        buildArray(doubleArrayMap,"double");
        buildArray(floatArrayMap,"float");
        buildArray(byteArrayMap,"byte");
        buildArray(charArrayMap,"char");
        buildArray(StringArrayMap,"String");

        intArrayMap.clear();
        longArrayMap.clear();
        shortArrayMap.clear();
        doubleArrayMap.clear();
        floatArrayMap.clear();
        byteArrayMap.clear();
        charArrayMap.clear();
        StringArrayMap.clear();        

        buildVectorDefinition(intMap);
        buildVectorDefinition(longMap);
        buildVectorDefinition(shortMap);
        buildVectorDefinition(doubleMap);
        buildVectorDefinition(floatMap);
        buildVectorDefinition(byteMap);
        buildVectorDefinition(charMap);
        buildVectorDefinition(StringMap);
        
        caseStr += ls+"    // static constructor"+ls;
        caseStr += "    static {" +ls;

        buildVectorAssignment(intMap, "int");
        buildVectorAssignment(longMap, "long");
        buildVectorAssignment(shortMap, "short");
        buildVectorAssignment(doubleMap, "double");
        buildVectorAssignment(floatMap, "float");
        buildVectorAssignment(byteMap, "byte");
        buildVectorAssignment(charMap, "char");
        buildVectorAssignment(StringMap, "String");
//        buildVectorAssignment(ObjectMap, "Object");        
        
        caseStr += "        LinkedOption.setSelected(\""+option+"\");"+ls;
        caseStr += "    }" +ls+ls;

        intMap.clear();
        longMap.clear();
        shortMap.clear();
        doubleMap.clear();
        floatMap.clear();
        byteMap.clear();
        charMap.clear();
        StringMap.clear();
                
        caseStr +=  "    ConanThread" + cases + " (" 
                  + (monitor.equals("")?"":monitor + " monitor,") 
                  + "MonitorClock clock, int id, String name, ConanSetup"
                  + cases + " shared) {" + ls
                  + "        super(name);" + ls
                  + "        this.clock = clock;" + ls
                  + "        this." + setupVals.get(cases-1) + " = shared;" + ls;
                                                                
        if (!monitor.equals("")) {
            caseStr += "        this." + var + " = monitor;" + ls;
        }
        
        caseStr += "        conan_Id = id;" + ls;      
        caseStr += "        LinkedOption.reset();"+ls;
        caseStr += "    }"                                       
                  + ls + ls
                  + "    public Thread thread(String id) { return ConanThread.getThread(id); }"                  
                  + ls + ls //+ beginCommand + ls + ls
                  + "    public void run() {" + ls + ls
                  + "        switch(conan_Id) {" + ls + ls;
        
        for (int i=0; i < uniqList.size(); ++i) {
            name = (String)uniqList.get(i);
            caseStr += "            case " + i + ":"+ls;          
            
            for (int j=0; j < cmdList.size(); ++j) {             

                String obj = (String)objList.get(j); 
            
                if (obj.equals(name)) {
                    caseStr += "                clock.await(" 
                        + ((Integer)(lineList.get(j))).toString() + ");" + ls
                        + "                conan_LastTick = clock.time();" + ls;
                        
                    String s = (String)optionMap.get(name+","+lineList.get(j));
                    if (s!=null) {
                        caseStr += "                if (LinkedOption.compareSelected(\""+s+"\")) " + ls;
                    }
                    caseStr += "                " + (String)cmdList.get(j) + ls;
                }
            }         
            caseStr += "                break;"+ls;            
        }                        
        caseStr += "            default: break;" + ls
                 + "        }" + ls
                 + "    }" + ls 
//                 + global
                 + "}" + ls;
        
        testCase.add(caseStr);           
        optionMap.clear();     

    }           
    
    private void parseTestConditions(StreamTokenizer st)
        throws Exception {
        
        int token; 
        String str = "";
        
        try {         
            while (true) {         
                str = getToken(st);

                if (isSpecialChar())
                    break;                       
                
                if (!str.equals(""))
                    testSet.add(str);
            }                                             
//            System.err.println(condMap.toString());
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                      
    }                    
    
    private void parseConditions(StreamTokenizer st) 
        throws Exception {
        
        int token; 
        String desc = "", str = "";
        String first = "";
        
        try { 
                
            st.eolIsSignificant(true);        

            while (true) {         
                str = getToken(st);

                if (isSpecialChar() || str.equals("TT_EOL")) {
                    if (!first.equals("")) {
                        condMap.put(first, desc);
                        first = "";
                        desc = "";
                    }
                    if (isSpecialChar())
                        break;      
                    continue;
                }                                          

                if (first.equals(""))
                    first = str;
                else
                    desc = desc + str + " ";                
            }                                             
            st.eolIsSignificant(false);
//            System.err.println(condMap.toString());
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                      
    }

    private void parseTop(StreamTokenizer st) 
        throws Exception {
        
        try { 
                                                       
            topCommand = parseCode(st); 
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                                    
    }
/*
    private void parseImport(StreamTokenizer st) 
        throws Exception {
        
        String str;
              
        try {               
            st.wordChars('*','*');
            while (true) {
                str = getToken(st);
                if (isSpecialChar())
                    break;
                if (str != null) {
                    out.println("import " + str);  
                }
            }                     
            st.ordinaryChar('*');                          
		}
		catch (Exception e) {
		    //e.printStackTrace();
		    throw e;
		}             		                                          
    }     
*/    
    private void parseDeadlock() {   
                                            
        deadlock = line;
    }                                  
    
    private boolean parseSetup(StreamTokenizer st, int cases) 
        throws Exception {
            
        String val = "", code = "";
        
        try { 
            st.ordinaryChar('>');
            st.ordinaryChar('<');                                       
            val = getToken(st);
            
            if (isSpecialChar())
                return true;
                            
            if (val.equals("<")) {
                val = getToken(st);
                String junk = getToken(st);
                if (!junk.equals(">")) {
                    syntax("invalid thread identifier. missing >.");
                    return false;
                } 
                setupVals.set(cases-1, val);               
            }
            else {                  
                code = val;
            }
            st.wordChars('>','>');
            st.wordChars('<','<'); 
                                                                                                  
            beginCommand = code + " " + parseCode(st); 
            beginCommand = "class ConanSetup" + cases 
                            + " extends ConanGlobalSetup {"
                            + ls + beginCommand + ls + "}";
            setup.set(cases-1, beginCommand);
            return true;
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                   
    }    
    
    private boolean parseGlobal(StreamTokenizer st) 
        throws Exception {
                        
        String val = "", code = "";
        
        try { 
            st.ordinaryChar('>');
            st.ordinaryChar('<');                                       
            val = getToken(st);
            
            if (isSpecialChar())
                return true;
            
            if (val.equals("<")) {
                val = getToken(st);
                String junk = getToken(st);
                if (!junk.equals(">")) {
                    syntax("invalid global identifier. missing >.");
                    return false;
                } 
                globalval = val;               
            }
            else {                  
                code = val;
            }
            st.wordChars('>','>');
            st.wordChars('<','<'); 
                                                                                                  
            global = code + " " + parseCode(st);
            return true;
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}              
/*        
        try { 
                                                       
            global = parseCode(st); 
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                   
*/
    } 
    
    private String parseDriver(StreamTokenizer st) 
        throws Exception {
              
        String str = null;
              
        try {
            while (str == null) {
                str = getToken(st);
/*                if (isSpecialChar())
                    break; */
                if (str != null) {
                    driver = str;   
                }
            }   
		}
		catch (Exception e) {
//		    e.printStackTrace();
            throw e;
		}             
		                                      
		return driver;
    }      
    
    private void parseSetOption(StreamTokenizer st) 
        throws Exception {
              
        String str = null;
              
        try {
            while (str == null) {
                str = getToken(st);
/*                if (isSpecialChar())
                    break; */
                if (str != null) {
                    option = str;   
                }
            }   
		}
		catch (Exception e) {
//		    e.printStackTrace();
		    throw e;
		}
    }
    
    private void parseTickTime(StreamTokenizer st) 
        throws Exception {
              
        String str = null;
              
        try {
            while (str == null) {
                str = getToken(st);
/*                if (isSpecialChar())
                    break; */
                if (str != null) {
                    ticktime = Integer.parseInt(str);   
                }
            }   
		}
		catch (Exception e) {
//		    e.printStackTrace();
		    throw e;
		}
    }
    
    
    private String parseOption(StreamTokenizer st) 
        throws Exception {
              
        String str = null;
              
        try {
            while (str == null) {
                str = getToken(st);
/*                if (isSpecialChar())
                    break; */
                if (str != null) {
                    return str;   
                }
            }   
		}
		catch (Exception e) {
//		    e.printStackTrace();
		    throw e;
		}
		return null;
    }
            
    private void buildDefinition(String driver) {

        out.println(
                    "import roast.*;" + ls +
                    "import conan.*;" + ls + ls +
                    "// START TOP CODE" + ls +
                    topCommand + ls +
                    "// END TOP CODE" + ls + ls +
                    "class ConanGlobalSetup extends ConanSetup {" + ls
                    + global + "}" + ls + ls +
                    "public class " + driver + " {" + ls + ls);
    }
    
    private void parseMonitor(StreamTokenizer st, int seq) 
        throws Exception {
             
        String str;              
        int i=0;
        
        try {
            st.ordinaryChar(')');
            st.ordinaryChar('(');                 
            for (i=0; i<2; ++i) {
                str = getToken(st);
                if (isSpecialChar())
                    break;
                if (i==0) 
                    var = str;
                else if (i==1) {
                    // take monitor from file if not specified on command line
//                    if (monitor.equals("")) {
                        monitor = str;
//                    }
                    constructor = parseCode(st).trim();
                    if (constructor.equals(""))
		                constructor = "()";	
		            Vector v = new Vector();
		            v.add(var);
		            v.add(monitor);
		            v.add(constructor);
		            monitorMap.put(new Integer(seq), v);
                }
		    }    
		    st.wordChars(')',')');
		    st.wordChars('(','(');
	//	    System.err.println("constructor: <" + constructor + ">");            
//		    if (constructor.equals(""))
//		        constructor = "()";		       		    
		}
		catch (Exception e) {
		    //e.printStackTrace();
		    throw e;
		}
    } 
    
    private String parseList(String type, StreamTokenizer st)
        throws Exception {

        int token; 
        String val = "";      
        
        List list = new ArrayList();
        
        try {
            st.ordinaryChar('>');
            st.ordinaryChar('<');                                       
            val = getToken(st);
            
            if (val.equals("<")) {
                val = getToken(st);
                String junk = getToken(st);
                if (!junk.equals(">")) {
                    syntax("invalid identifier. missing >.");
                    return null;
                }                
            }                      
            else {
                if ((char)st.ttype == '\'')
                    val = '\'' + val + "'";                
                else if ((char)st.ttype == '"')
                    val = '"' + val + "\"";
                                 
                list.add(val);    
                val = type+"List";      
            }
            st.wordChars('>','>');
            st.wordChars('<','<');                     

            String str = "";        
       
            while (true) {         
                str = getToken(st);

                if (isSpecialChar())
                    break;
                    
                if ((char)st.ttype == '\'')
                    str = '\'' + str + "'";                
                else if ((char)st.ttype == '"')
                    str = '"' + str + "\"";                                                   
                
                if (!str.equals("")) {
                    list.add(str);    
                }                                    
		    }             

		    if (list.size()==0) {
                syntax("no entries specified. Zero length list.");
                return null;		        
		    }                     
		    if (type.equals("int")) intMap.put(val,list);
		    else if (type.equals("char")) charMap.put(val,list);
		    else if (type.equals("byte")) byteMap.put(val,list);
		    else if (type.equals("float")) floatMap.put(val,list);
		    else if (type.equals("double")) doubleMap.put(val,list);
		    else if (type.equals("long")) longMap.put(val,list);
	        else if (type.equals("short")) shortMap.put(val,list);
	        else StringMap.put(val,list);
		    return val;
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                           
                    
    }     
    
    private String parseArray(String type, StreamTokenizer st)
        throws Exception {

        int token; 
        String val = "";      
        
        List list = new ArrayList();
        
        try {
            st.ordinaryChar('>');
            st.ordinaryChar('<');                                       
            val = getToken(st);
            
            if (val.equals("<")) {
                val = getToken(st);
                String junk = getToken(st);
                if (!junk.equals(">")) {
                    syntax("invalid identifier. missing >.");
                    return null;
                }                
            }                      
            else {
                if ((char)st.ttype == '\'')
                    val = '\'' + val + "'";                
                else if ((char)st.ttype == '"')
                    val = '"' + val + "\"";
                                 
                list.add(val);    
                val = type+"List";      
            }
            st.wordChars('>','>');
            st.wordChars('<','<');                     

            String str = "";        
       
            while (true) {         
                str = getToken(st);

                if (isSpecialChar())
                    break;
                    
                if ((char)st.ttype == '\'')
                    str = '\'' + str + "'";                
                else if ((char)st.ttype == '"')
                    str = '"' + str + "\"";                                                   
                
                if (!str.equals("")) {
                    list.add(str);    
                }                                    
		    }             

		    if (list.size()==0) {
                syntax("no entries specified. Zero length array.");
                return null;		        
		    }                     
		    if (type.equals("int")) intArrayMap.put(val,list);
		    else if (type.equals("char")) charArrayMap.put(val,list);
		    else if (type.equals("byte")) byteArrayMap.put(val,list);
		    else if (type.equals("float")) floatArrayMap.put(val,list);
		    else if (type.equals("double")) doubleArrayMap.put(val,list);
		    else if (type.equals("long")) longArrayMap.put(val,list);
	        else if (type.equals("short")) shortArrayMap.put(val,list);
	        else StringArrayMap.put(val,list);
		    return val;
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                           
                    
    }     

        
    private String parseLine(StreamTokenizer st, String option) 
        throws Exception {
        
        int token; 
        String command = "";
        String code = "", val = "";      
        boolean append = false;
        
        try {
            st.ordinaryChar('>');
            st.ordinaryChar('<');                                       
            val = getToken(st);
            
            if (val.equals("<")) {
                val = getToken(st);
                String junk = getToken(st);
                if (!junk.equals(">")) {
                    System.err.println("invalid thread identifier. missing >.");
                    return null;
                }
//                st.wordChars('>','>');
//                st.wordChars('<','<');                    
            }                      
            else {                          
//                st.wordChars('>','>');
//                st.wordChars('<','<');
                append = true;     
                code = val;
                thread_count++;
                val = "_thr_" + thread_count;  // thread name
            }
            st.wordChars('>','>');
            st.wordChars('<','<');
//          System.err.println(val);
//            if (isSpecialChar())
//                return;   
//          System.err.println(val);                        
            
            command = parseCode(st);               
            
            if (append)
                command = code + " " + command;

            lineList.add(new Integer(line));
            objList.add(val); 
            cmdList.add(command);    
            if (option!=null && !option.equals("")) {
                optionMap.put(val + "," + line,option);
            }
            if (!uniqList.contains(val))
                uniqList.add(val);
            return val;
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}                   
    }            

    private String parseCode (StreamTokenizer st) throws IOException {
        
        String str, command = "";
                    
        st.eolIsSignificant(true);                   
                      
        while (true) {         
            
            str = getToken(st);

            if (isSpecialChar())
                break;            
                    
            if (str.equals("TT_EOL"))
                str = ls; 
                    
            if ((char)st.ttype == '\'')
                command = command + '\'' + str + "' ";
                
            else if ((char)st.ttype == '"')
                command = command + '"' + str + "\" ";                
            else    
                command = command + str + " ";
        }       
        st.eolIsSignificant(false);
        
        return command;
    }                       
/***    
    private void parseTeardown(StreamTokenizer st) 
        throws Exception {
        
        try { 
            endCommand = parseCode(st); 
		}    
		catch (Exception e) {
		    st.eolIsSignificant(false);
		    //e.printStackTrace();
		    throw e;
		}      
    }            
***/        
    private String getToken(StreamTokenizer st) throws IOException {
        
        strToken = null;
        int token;
        
        if ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
            
            if (token == StreamTokenizer.TT_EOL)
              strToken="TT_EOL";
            else
              strToken=(st.sval == null?String.valueOf((char)st.ttype):st.sval);         
        }
        
        return strToken;
    }                  
    
    private boolean isSpecialChar() {
        return (strToken == null || strToken.equals(CONTROL));        
    }                  
    
    public void finish() {              
        
        out.println("    public static void main(String[] args) {");       
        for (int i=1; i < cases+1; ++i) {
            out.println("        sequence" + i + "();");
        }  
        out.println("        Roast.printCounts();");         
        out.println("        System.out.println(\"***** Liveness " 
                                +   "errors: \"+ ConanHelper.deadlockEvents());");
        out.println("        System.exit(1);");        
        out.println("    }"); 
        out.println("}" + ls + ls);           
            
        for (int i=0; i < testCase.size(); ++i) {
            out.println(testCase.get(i));
        }
        
        for (int i=0; i < setup.size(); ++i) {
            out.println(setup.get(i));
        }
            
        Iterator it = condMap.keySet().iterator();
        while (it.hasNext()) {
            String cond = (String)it.next();
            if (!testSet.contains(cond))
                warn("Condition " + cond 
                        + " does not appear in a test sequence");
        }  

        out.close();
    }                
}
