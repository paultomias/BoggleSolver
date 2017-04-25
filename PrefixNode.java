import java.util.*;
 
class PrefixNode 
{
    char content; 
    boolean isEnd; 
    int count;  
    LinkedList<PrefixNode> children; 
 
    public PrefixNode(char c){
        children = new LinkedList<PrefixNode>();
        isEnd = false;
        content = c;
        count = 0;
    }  
    public PrefixNode subNode(char c){
        if (children != null)
            for (PrefixNode child : children)
                if (child.content == c)
                    return child;
        return null;
    }
}