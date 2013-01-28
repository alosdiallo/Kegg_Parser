/*
 * This program will take in a file and generate a new file with the following
 * Gene Name
 * Orthology
 * Organism name
 * Other DB
 * NT
 * AA
 *
 */

package get_info_kegg;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Alos
 */
public class Get_info_Kegg {
   /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {

        //Creating a new instance of Get_info_Kegg so that I can call get_contents
        Get_info_Kegg g=new Get_info_Kegg();
        g.read_file();   
    }
    
    
    public void get_contents(BufferedReader in, String name,String s) throws IOException{
        String inputLine;
        ArrayList<String> sequence_info = new ArrayList<>();
        name = name.toLowerCase();
        String url_search = "/dbget-bin/www_bget?";
        StringBuilder strBuf = new StringBuilder();
        strBuf.append(url_search);
        strBuf.append(name);
        
        while ((inputLine = in.readLine()) != null) {

            if(inputLine.contains(strBuf))
            {
                    
                    Get_info_Kegg testK = new Get_info_Kegg();
                    String[] splits_hrf = inputLine.split(name + ":");
                    String[] splits_hrf_small = splits_hrf[1].split("\">");
                    String latter_half_of_url = splits_hrf_small[0];
                    System.out.println(latter_half_of_url);
                    URL keg_url_bget = null;
                    keg_url_bget = new URL("http://www.genome.jp/");
                    ArrayList<String> url_list = new ArrayList<>();
                    String newLine = System.getProperty("line.separator"); 
                    String[] buffer_split = inputLine.split(name);

                    for(String i : buffer_split){
                        
                        String[] gene_id_info = i.split("\">");
                        gene_id_info = gene_id_info[0].split(":");
                        //System.out.println(buffer_split_new[1] + "first" + i + "last" );
                        if(!gene_id_info[1].contains(";")){
                            String usrl_string =  gene_id_info[1];                      
                            url_list.add(usrl_string);
                            
                        }
                        
                    }
                    

                    
                    for(String j : url_list){
                            
                                             
                                //System.out.println("Lookout " + j + newLine);
                                StringBuilder strBufURL_aa = new StringBuilder();
                                StringBuilder strBufURL_nt = new StringBuilder();
                                StringBuilder strBufURL_up = new StringBuilder();
                                strBufURL_aa.append("http://rest.kegg.jp/get/");
                                strBufURL_aa.append(name);
                                strBufURL_aa.append(":");
                                strBufURL_aa.append(j);
                                strBufURL_aa.append("/aaseq");
                                strBufURL_nt.append("http://rest.kegg.jp/get/");
                                strBufURL_nt.append(name);
                                strBufURL_nt.append(":");
                                strBufURL_nt.append(j);
                                strBufURL_nt.append("/ntseq");
                                strBufURL_up.append("http://rest.kegg.jp/conv/UniProt/");
                                strBufURL_up.append(name);
                                strBufURL_up.append(":");
                                strBufURL_up.append(j);
                                String aa_url = strBufURL_aa.toString();
                                String nt_url = strBufURL_nt.toString();
                                String up_url = strBufURL_up.toString();
                                if(!sequence_info.contains(aa_url)){
                                    if(!sequence_info.contains(nt_url)){
                                       
                                            sequence_info.add(aa_url);
                                            sequence_info.add(nt_url);
                                            sequence_info.add(up_url);
                                            testK.build_data_from_url(sequence_info,name,j);
                                            System.out.println(sequence_info);
                                            System.out.print(newLine);
                                            
                                        
                                    }
                                }
                                sequence_info.removeAll(sequence_info);
                            
                    }   
            } 
      } 

      in.close();   
    }
 
    public void build_data_from_url(ArrayList<String> sequence_info, String name, String id) throws MalformedURLException, IOException{
        Get_info_Kegg K = new Get_info_Kegg();
        for(String s :sequence_info){

            if (s.contains("aaseq")){
                String aaSequence = s.toString();
                URL aa = new URL(aaSequence);
                BufferedReader aa_buffer = new BufferedReader(new InputStreamReader(aa.openStream()));
                K.keg_test(aa_buffer,"AA");  

            }
            if (s.contains("ntseq")){
                String aaSequence = s.toString();
                URL nt = new URL(aaSequence);
                BufferedReader nt_buffer  = new BufferedReader(new InputStreamReader(nt.openStream()));
                K.keg_test(nt_buffer,"NT");

            }            
            if (s.contains("UniProt")){
                String aaSequence = s.toString();
                URL up = new URL(aaSequence);
                BufferedReader up_buffer  = new BufferedReader(new InputStreamReader(up.openStream()));
                K.keg_test(up_buffer,"UniProt");

                
            } 
            
             
        }

    }

    //This is used to create the outfile
    public void keg_test(BufferedReader buffer_name, String file_type) throws IOException{
            
            String newLine = System.getProperty("line.separator"); 
            File log = new File("/home/dialloa/"+file_type+"_result.txt");
            String inputLine;
            if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }
            while ((inputLine = buffer_name.readLine()) != null) {
                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                  System.out.println(inputLine);
                  out.append(inputLine);
                  out.append(newLine);       
                  out.close();
                 
            }
            buffer_name.close();    
    }    

    public void read_file(){
        try{
           
        // Open the file that is the first 
        // command line parameter
        FileInputStream fstream = new FileInputStream("/home/dialloa/orgVSecforalos.txt");
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
             
            String[] splits = strLine.split("\t");
            String name = splits[0];
            for(String s : splits){        
                if (s.matches("[0-9+/. ()-]+")) {         
                    Get_info_Kegg g=new Get_info_Kegg();
                    g.url_maker(s,name);
                     
                   //System.out.println (s); 
                }    
            }  
        }
        //Close the input stream
        in.close();
        
          }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        }
  
    }
  
    //Get Kegg EC number and make a url from it.
    public void url_maker(String s,String name) throws MalformedURLException, IOException{
        String keg_url = "http://www.genome.jp/dbget-bin/";
        String keg_ec_two = "www_bget?ec:";
        keg_ec_two = keg_ec_two.concat(s);
        keg_url = keg_url.concat(keg_ec_two);
        URL keg_ec = new URL(keg_url);
        System.out.println("******** Species: " + name + " EC: " + s + " ********" );
        BufferedReader in = new BufferedReader(
        new InputStreamReader(keg_ec.openStream()));
        Get_info_Kegg g=new Get_info_Kegg();    
        g.get_contents(in, name,s);

    }

    private String toString(StringBuilder strBuild) {
        throw new UnsupportedOperationException("Not yet implemented");
        
    }
 
}
