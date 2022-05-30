import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static Map<Integer, Character> dictOriginal;
    static Map<Integer, Character> dictionary;
    static Integer key;
    static Character decodeOrEncode;
    static String decodeOrEncodeWord;
    static Character offset;
    static String mySentence;
    static String modifiedSentence;
    static boolean doWeContinue=true;
    public static void main(String[] args) throws IOException {
        dictOriginal = referenceTable(); //1. construct original dictionary Key-Value

        do{
            System.out.print("Pick your offset: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            offset = br.readLine().toUpperCase().charAt(0);//2. getting intended offset

            key = getKey(dictOriginal,offset);//3. getting the key of corresponsing offset

            System.out.print("Press d for decode, e for encode: ");//4. getting decode or encode
            decodeOrEncodeWord = br.readLine();
            if(decodeOrEncodeWord.length()==1){
                decodeOrEncode = decodeOrEncodeWord.charAt(0);
            }
            else
            {
                System.out.print("You did not press d or e, restart? Y/N");
                String response = br.readLine().toUpperCase();
                if(response.equals("Y")){
                    continue;
                }
                else{
                    break;
                }
            }


            System.out.print("Type your sentence: ");
            mySentence = br.readLine();

            if(decodeOrEncode == 'd'){//5a. decode the sentence
                modifiedSentence = decode(mySentence,offset);
                System.out.println("Your Decoded word is "+modifiedSentence);
            }
            else if(decodeOrEncode =='e'){//5b. encode the sentence
                modifiedSentence = encode(mySentence,offset);
                System.out.println("Your encoded word is "+modifiedSentence);
            }
            else
            {
                System.out.println("You didn't press d or e");
            }
            System.out.print("Restart? Y/N");
            String response = br.readLine().toUpperCase();//6. go back to step 2 or exit
            if(response.equals("Y")){
                doWeContinue = true;
                continue;
            }
            else{
                doWeContinue=false;
            }
        }while(true);

    }
    //plaintext to meaningless=encode

    static String encode(String plainText, Character offset){
        String[] characterArray = plainText.split("");
        dictionary = generalTable(offset);//1. getting the offset-ed dictionary
        String word = "";
        for(String s : characterArray){
            String s1="";
            if(!s.isBlank())
            {
                Integer x = getKey(dictOriginal,s.toUpperCase().charAt(0));//2. getting the keys based on original dictionary
                Character c =dictionary.get(x);//3a. Translate the corresponding key to the value in offset-ed dict
                s1 = String.valueOf(c);
            }
            else {
                s1=s;// 3b. if it is space, no need translate
            }

            word += s1; //4. store the word
        }

        return word;
    }

    //meaningless to plain=decode
    static String decode(String encodedText, Character offset){
        String[] characterArray = encodedText.split("");
        dictionary = generalTable(offset);//1. getting the offset-ed dictionary
        String word = "";
        for(String s : characterArray){
            String s1="";

            if(!s.isBlank()){
                Integer x = getKey(dictionary,s.toUpperCase().charAt(0));//2. getting the keys based on offset-ed dictionary
                Character c =dictOriginal.get(x);//3a. Translate the corresponding key to the value in original dict
                s1 = String.valueOf(c);
            }
            else {
                s1=s;// 3b. if it is space, no need translate
            }
            word +=s1;//4. store the word
        }

        return word;
    }

    public static Map<Integer, Character> referenceTable(){
        dictOriginal = new HashMap<>();
        for(int i=0;i<44;i++){
            if(i<26){
                dictOriginal.put(i,(char)(i+'A'));
            }
            else if(i<36){
                dictOriginal.put(i,(char)(i-26+'0'));
            }
            else{
                dictOriginal.put(i,(char)(i-36+'('));
            }
        }
        return dictOriginal;

        //  0 to 25 is A to Z     ASCII 65 to 90
        //  26 to 35 is 0 to 9    ASCII 48 to 57
        //  36 to 43 is ()*+,-./  ASCII 40 to 47
    }

    public static Integer getKey(Map<Integer, Character> index, Character value){
        List<Integer> key = index
                .entrySet()
                .stream()
                .filter(e-> Objects.equals(e.getValue(),value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return key.get(0);
    }
    public static Map<Integer, Character> generalTable(Character offset){
        Map<Integer, Character> dictionary = new HashMap<>();
        Integer offsetInt = getKey(referenceTable(),offset);

        for(int i=0+offsetInt;i<44+offsetInt;i++){
            int x=i-offsetInt;
            if(i<26+offsetInt){
                dictionary.put(i%44,(char)(x+'A'));
            }
            else if(i<36+offsetInt){
                dictionary.put(i%44,(char)(x-26+'0'));
            }
            else{
                dictionary.put(i%44,(char)(x-36+'('));
            }
        }
        return dictionary;
    }
}
