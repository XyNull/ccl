import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.lang.System;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {        
	private static String Usage =
			"Multifunction file manager .\n" +
					//"Usage: <ChooseFunction> <RootFolder> <Addons> [<Suffix>,<FileName>,...] [<Options>]\n\n" +
					"Function:\n" +
					"CountLines or CL    for counting code line's count.\n" +
					"CountBytes or CB    for counting files' size by paths.\n"+
					"SearchFile or SF    for searching specific file.\n\n"+
					"RootFolder: Folder to count.\n" + 
					"ex. D:\\LeetCode \n\n" + 
					"Addons:\n"+
					"Suffix: Suffix of code file.Start with '.' for CountLines.\n" +
					"ex. 。java 。cpp 。cs \n" +
					"FileName: The name of what you search with or without suffix or only suffix.\n"+
					"ex. test.txt or test or .avi\n\n"+
					"Options:\n" +
					"-I    Ignore blank files & path in log.\n" +
					"-V    Show process log.\n\n" +
					"PS.Ignoring blank lines is default\n\n";
	private static String addonsUsage = 
			"please input the addons (separated by enter):" +
			"input \"over\" to end";
	
    private static String rootFolder;

    public static ArrayList<String> suffixs = new ArrayList<String>();

    private static boolean ignoreBlank = false;

    private static boolean verbose = false;
    
    public static void forCL(List<String> args){
    	//progress arguments for CL
        for(String str : args){
        	if(str.startsWith("."))
        		suffixs.add(str);
        	if(str.equals("-V"))
        		verbose = true;
        	if(str.equals("-I"))
        		ignoreBlank = true;
        }
    }
    
    public static void main(String[] args){
    	long start = 0;
    	System.out.println(Usage);
    	
    	System.out.println("please input rootFolder:\n");
        Scanner in = new Scanner(System.in);
    	String rf = in.nextLine();
    	
        //progress rootFolder
        if(rf.charAt(rf.length()-1) != '\\')
        	rootFolder = rf;
        else
        	rootFolder = rf.substring(0, rf.length()-2);
    	
        System.out.println("please choose the funtion:");
    	String function = in.nextLine();
    	
        //progress addons by each function
        if(function.equals("CL") || function.equals("CountLines")){
        	System.out.println(addonsUsage);
        	
        	List<String> addons = new ArrayList<String>();
        	while(in.hasNext()) {
        		if(in.nextLine().equals("over")) break;
        		addons.add(in.nextLine());
        	}
        	in.close();
        	
        	forCL(addons);
            start = new Date().getTime();
            System.out.println("Total:" + VisitDirectory(rootFolder) + " line(s).");
        }
        
        else if(function.equals("CB") || function.equals("CountBytes")){
        	
            start = new Date().getTime();
        }
        else if(function.equals("SF") || function.equals("SearchFile")){
        	
        }
        else {
        	System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nAttetion:input error,please input again.\n");
    		main(new String[1]);
    		return;
        }
        
        long time = new Date().getTime() - start;
        
        System.out.println("Time: " + time + "ms.");
    }
    
	public static int VisitFile(String path){
    	Charset cs = Charset.defaultCharset();
    	Path p = Paths.get(path);
    	
        try{
        	List<String> lines = Files.readAllLines(p,cs);
        	while(lines.remove(""));
            int count = lines.size();

            if (verbose)
            {
            	if((ignoreBlank && count != 0) || !ignoreBlank)
            			System.out.println(path.toString() + " -- " + count + " line(s).");
            }

            return count;
        }
        catch (Exception e){
            if (verbose)
            {
            	System.out.println("Can't read " + path);
            }
            return 0;
        }
    }
	
	public static int VisitDirectory(String path){
        try{
            String[] dirs = getDirectories(path);
            int count=0; 
            for(int i = 0; i < dirs.length; i++)
            {
            	count += VisitDirectory(dirs[i]);
            }

            File[] files = getAllFiles(path);
            
            for(int i = 0; i < files.length; i++)
            {
            	if(suffixPicker(files[i].getName()))
            		count += VisitFile(files[i].toString());
            }

            if (verbose)
            {	if((ignoreBlank && count != 0) || !ignoreBlank)
            		System.out.println(path.toString() + " -- " + count +" line(s).");
            }
            return count;
        }
        
        catch (Exception e)
        {
            if (verbose)
            {
            	System.out.println("Can't read " + path);
            }
            return 0;
        }
	}
	
	public static boolean suffixPicker(String name) {
		for(String suf : suffixs){
			if (name.toLowerCase().endsWith(suf))
				return true;
		}
		return false;
	}
	
	public static String[] getDirectories(String path) {
		if (!pathIsEmpty(path)) {
			return null;
		}

		File[] files = new File(path).listFiles();
		List<String> temp = new ArrayList<String>();
		
		for (File file : files) {
			if (file.exists() && file.isDirectory()) {
				temp.add(file.toString());
			}
		}
		
		String[] dirs = new String[temp.size()];
		int i = 0;
		for(String str:temp)
			dirs[i++] = str;
		return dirs;
	}
	
	public static boolean pathIsEmpty(String path) {
		if (path == null || path.length() < 1) {
			System.err.println("ERROR:给定路径 "+ path +" 不能为空。");
			return false;
		} else if (!new File(path).exists()) {
			System.err.println("ERROR:给定路径 "+ path +" 不存在。");
			return false;
		} else {
			return true;
		}
	}
	
	public static File[] getAllFiles(String path) {

		List<File> temp = new ArrayList<File>();
		File rootPath = new File(path);
		File[] items = rootPath.listFiles();
		for (int i = 0; i < items.length; i++) {
			if (items[i].exists()) {
				if (items[i].isFile()) 
					temp.add(items[i]);
				else if (items[i].isDirectory()) 
					getAllFiles(items[i].getPath());
				
			}
		}
		
		File[] files = new File[temp.size()];
		int i = 0;
		for(File f:temp)
			files[i++] = f;
		
		return files;
	}
}
