import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {        
	private static String Usage =
			"Utility for counting code line's count.\n" +
					"Usage: ccl <RootFolder> <Suffix> [<Suffix>,...] [<Options>]\n\n" +
					"RootFolder: Folder to count.\n" + 
					"ex. D:\\LeetCode \n\n" + 
					"Suffix: Suffix of code file.Start with '.'.\n" +
					"ex. 。java 。cpp 。cs \n\n" +
					"Options:\n" +
					"-I    Ignore blank files & path.\n" +
					"-V    Show process log.\n\n" +
					"PS.Ignore blank lines is default\n";
	
    private static String rootFolder;

    public static ArrayList<String> suffixs = new ArrayList<String>();

    private static boolean ignoreBlank = false;

    private static boolean verbose = false;
    
    public static boolean ProcessArguments(String[] args){
        if (args.length < 1)
            return false;

        if(args[0].charAt(args[0].length()-1) != '\\') 
        	rootFolder = args[0];
        else 
        	rootFolder = args[0].substring(0, args[0].length()-2);
        
        for(String str : args){
        	if(str.startsWith("."))
        		suffixs.add(str);
        	if(str.equals("-V"))
        		verbose = true;
        	if(str.equals("-I"))
        		ignoreBlank = true;
        }

        return true;
        }
    
    public static void main(String[] args){
        long start = new Date().getTime();

        if (!ProcessArguments(args))
        {
        	System.out.println(Usage);
            return;
        }
         
        System.out.println("Total:" + VisitDirectory(rootFolder) + " line(s).");
        
        long time = new Date().getTime() - start;

        System.out.println("Time: " + time + "ms.");
    }
    
	public static int VisitFile(String path){
    	Charset cs = Charset.forName("UTF-8");
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
            String[] dirs = GetDirectories(path);
            int count=0; 
            for(int i = 0; i < dirs.length; i++)
            {
            	count += VisitDirectory(dirs[i]);
            }

            File[] files = getAllFiles(path);
            
            for(int i = 0; i < files.length; i++)
            {
            	if(FilePicker(files[i].getName()))
            		count += VisitFile(files[i].toString());
            }

            if (verbose)
            {
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
	
	public static boolean FilePicker(String name) {
		for(String suf : suffixs){
			if (name.toLowerCase().endsWith(suf))
				return true;
		}
		return false;
	}
	
	public static String[] GetDirectories(String path) {
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
