class PrefixTree
{
    private PrefixNode root;
 
    public PrefixTree(){
        root = new PrefixNode(' '); 
    }

    /*Inserts a word into the prefix tree*/
    public void insert(String word){
        if (searchWord(word) == true) 
            return;        
        PrefixNode current = root; 
        for (char ch : word.toCharArray() ){
            PrefixNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else{
                 current.children.add(new PrefixNode(ch));
                 current = current.subNode(ch);
            }
            current.count++;
        }
        current.isEnd = true;
    }
    
    /*Searches if a prefix is present in the prefix tree*/
    public boolean searchPrefix(String word){
        PrefixNode current = root;  
        for (char ch : word.toCharArray() ){
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }      
        return true;
    }

    /*Searches for a word in the prefix tree*/
    public boolean searchWord(String word){
        PrefixNode current = root;  
        for (char ch : word.toCharArray() ){
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }      
        if (current.isEnd == true) 
            return true;
        return false;
    }
}