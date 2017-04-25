/*
Class which contains the boggle board UI and handles the gameplay and logic
*/
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.CardLayout.*;
import java.awt.event.*;
import java.awt.event.ActionEvent.*;
import java.awt.event.KeyListener.*;
import java.io.*;
import java.lang.*;
import javax.swing.UIManager.*;
import java.util.Comparator;
import java.util.Arrays;
import java.awt.CardLayout.*;

public class GameFrame extends JFrame implements ActionListener, KeyListener{
	JTextArea wordList;
	JTextField answer;
	JButton submit,solve,left,right;
	JScrollPane scroll;
	JLabel label1;
	GamePanel panel;
	String filename="sample.txt";
	int numBoard,count,nb,cPointer=1;
	ArrayList<String> words;
	ArrayList<String> wordSubmit = new ArrayList<String>();
	
	CardLayout cLayout;
	JPanel cHolder;
	JPanel[] boardPanel;
	
	public GameFrame(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception e) {
			System.out.println("Nimbus theme not available");
			System.out.println("Remove line 24 to 35");
		}
	
		this.setTitle("Boggle");
		this.setSize(950,550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		panel = new GamePanel();
		this.add(panel);
		
		Font font = new Font("Verdana", Font.BOLD, 15);
		
		cLayout = new CardLayout();
		cHolder = new JPanel(cLayout);
		cHolder.setVisible(true);
		cHolder.setBounds(85,50,400,400);
		panel.add(cHolder);
		
		left = new JButton("PREVIOUS");
		left.setBounds(125,10,120,30);
		left.addActionListener(this);
		left.setForeground(new Color(255,155,51));
		left.setBackground(Color.WHITE);
		left.setFont(font);
		panel.add(left);
		
		right = new JButton("NEXT");
		right.setBounds(320,10,120,30);
		right.addActionListener(this);
		right.setForeground(new Color(255,155,51));
		right.setBackground(Color.WHITE);
		right.setFont(font);
		panel.add(right);
		
		
		
		label1 = new JLabel("POSSIBLE WORDS");
		label1.setBounds(690,10,230,30);
		label1.setFont(font);
		label1.setForeground(Color.WHITE);
		panel.add(label1);
		
		wordList = new JTextArea();
		wordList.setEditable(false);
		wordList.setFont(font);
		wordList.setBackground(new Color(44,197,166));
		wordList.setForeground(Color.WHITE);
		
		scroll = new JScrollPane(wordList);
		scroll.setBounds(650,40,250,400);
		panel.add(scroll);
		
		solve = new JButton("SOLVE");
		solve.setBounds(730,470,100,30);
		solve.setBackground(new Color(44,197,166));
		solve.setForeground(Color.WHITE);
		solve.addActionListener(this);
		panel.add(solve);
	
		createBoard();
	
		answer = new JTextField();
		answer.setBounds(115,470,250,30);
		answer.setBackground(new Color(255,155,51));
		answer.setForeground(Color.WHITE);
		answer.setFont(new Font("Verdana", Font.BOLD, 13));
		answer.addKeyListener(this);
		panel.add(answer);
		
		submit = new JButton("SUBMIT");
		submit.setBackground(new Color(255,155,51));
		submit.setForeground(Color.WHITE);
		submit.setBounds(375,470,100,30);
		submit.addActionListener(this);
		panel.add(submit);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
        	String input = answer.getText().toUpperCase();
			boolean found = false;

			if(!input.equals("")){
				for(String str:wordSubmit)
					if(str.equals(input)){
						found = true;
						break;
					}
				if(!found) wordSubmit.add(input);
			}
			answer.setText("");
        }
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==solve){
			readFile();
			wordSubmit.clear();
		}
		if(e.getSource()==submit){

			String input = answer.getText().toUpperCase();
			boolean found = false;

			if(!input.equals("")){
				for(String str:wordSubmit)
					if(str.equals(input)){
						found = true;
						break;
					}
				if(!found) wordSubmit.add(input);
			}
			answer.setText("");
		}
		if(e.getSource()==left){
			if(cPointer > 1){
				cPointer = cPointer - 1;
				cLayout.show(cHolder,"Card"+cPointer);
			}
			else{
				cPointer = nb;
				cLayout.show(cHolder,"Card"+cPointer);
			}
		}
		if(e.getSource()==right){
			if(cPointer < nb){
				cPointer = cPointer + 1;
				cLayout.show(cHolder,"Card"+cPointer);
			}
			else{
				cPointer = 1;
				cLayout.show(cHolder,"Card"+cPointer);
			}
		}
		answer.grabFocus();
	}
	
	/*Reads sample.txt and determines the number of input cases and board sizes*/
	public void readFile(){
		int bCounter,size,i,j,score;
		char[][] board;
		String line;
		String[] temp;
		
		try{
			FileReader inputFile = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			
			numBoard = Integer.parseInt(bufferReader.readLine());
			for(bCounter=0;bCounter<numBoard;bCounter++){
				size = Integer.parseInt(bufferReader.readLine());
				board = new char[size][size];
				words = new ArrayList<String>();
				score = 0;
				for(i=0;i<size;i++){
						line = bufferReader.readLine();
						temp = line.split(" ");
						for(j=0;j<size;j++){
							board[i][j] = temp[j].charAt(0);
						}
					}
				boggleSolver(board,words,size);
				
				if(cPointer-1==bCounter){
					displayResult(words,bCounter+1);
					wordList.append("\n\nYour Correct Answers:\n\n");
					for(String ans:wordSubmit)
						for(String str:words)
							if(str.equals(ans)){
								wordList.append(ans+"\n");
								score++;
							}
					
					wordList.append("\nTOTAL SCORE: "+score);
				}
				count=0;
			}
			bufferReader.close();	
			
		}
		catch(Exception e){
			System.out.println("Error reading file");	
		}
	}
	
	/*Displays the solution for the boggle on the right panel*/
	public void displayResult(ArrayList<String> words,int counter){
			int i;	
			wordList.setText("");
			if(wordList.getText().compareTo("") == 0){	//when text area is still empty
				wordList.setText("Case 1: " + words.size() + " Solutions");
				for(i=0;i<words.size();i++){
					wordList.setText(wordList.getText()+"\n"+words.get(i));
				}
			}
			else{
				wordList.setText(wordList.getText()+"\n\n"+"---------------------------\n"+"Case "+counter+":"+words.size()+" Solutions");
				for(i=0;i<words.size();i++){
					wordList.setText(wordList.getText()+"\n"+words.get(i));
				}
			}
	}
	
	public void createBoard(){
		JButton[][] buttons;
		int i,j,c,size2;
		String[][] board2;
		String[] temp2;
		String line2;
		
		Font font2 = new Font("Verdana", Font.BOLD, 20);

		try{
			FileReader inputFile2 = new FileReader(filename);
			BufferedReader bufferReader2 = new BufferedReader(inputFile2);
			
			nb = Integer.parseInt(bufferReader2.readLine());
			boardPanel = new JPanel[nb];
			for(c=0;c<nb;c++){
				size2 = Integer.parseInt(bufferReader2.readLine());
				boardPanel[c] = new JPanel();
				boardPanel[c].setLayout(new GridLayout(size2,size2));
				boardPanel[c].setBackground(new Color(255,155,51));
				buttons = new JButton[size2][size2];
				for(i=0;i<size2;i++){
					line2 = bufferReader2.readLine();
					temp2 = line2.split(" ");
					for(j=0;j<size2;j++){
						buttons[i][j] = new JButton(temp2[j]);
						buttons[i][j].setForeground(new Color(255,155,51));
						buttons[i][j].setBackground(Color.WHITE);
						buttons[i][j].setFont(font2);
						boardPanel[c].add(buttons[i][j]);
					}
				}
				cHolder.add(boardPanel[c],"Card"+(c+1));
			}
		}
		catch(Exception e){
		}
	}
	
	/*Main function that searches for possible answers in the boggle board*/
	public void boggleSolver(char[][] board,ArrayList<String> words,int boardSize){
		
		ArrayList nopts = new ArrayList();
        ArrayList<BogglePiece> visited = new ArrayList<BogglePiece>();
        ArrayList<ArrayList<BogglePiece>> options = new ArrayList<ArrayList<BogglePiece>> ();
        ArrayList<BogglePiece> row = new ArrayList<BogglePiece>();
        StringCompare stringcompare = new StringCompare();
        PrefixTree t = new PrefixTree();
        int r, c, top, start = 0, size=boardSize, count = 0, index = size*size;
        boolean found, backtrack = false;

        final File file = new File("dictionary.txt");  //reads all the words in the dictionary
        try{
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                t.insert(line);         //inserts the word into the trie
            }
        }
        catch (FileNotFoundException e){
            
        } 

       /*Creates Initial Row*/
       for(int i = -1, j = 0; j<size*size; j++){
            if(j%size==0) i++;
            BogglePiece piece = new BogglePiece(board[i][j%size], i,j%size);
            row.add(piece);     //adds all the boggle pieces to the first row 
        }
        options.add(row);
        nopts.add(options.get(0).size()); //adds size of row
        visited.add(options.get(0).get(0)); //adds a visited node

        start = (int)nopts.get(0);

       while(nopts.size() != 0){
            ArrayList<BogglePiece> new_row = new ArrayList<BogglePiece>();
        
            r = visited.get(visited.size()-1).getRowIndex(); //new_row index of current node
            c = visited.get(visited.size()-1).getColIndex(); //column index of current node
            new_row.clear();

            /*Checks all the letters adjacent to the current node*/
            if(r!=0 && c-1!=-1){
                if(notFound(r-1,c-1,visited)){
                    BogglePiece piece = new BogglePiece(board[r-1][c-1],r-1,c-1);
                    new_row.add(piece);
                }
            }

            if(r!=0){
                if(notFound(r-1,c,visited)){
                    BogglePiece piece = new BogglePiece(board[r-1][c],r-1,c);
                    new_row.add(piece);
                }
            }

            if(r!=0 && c+1!=size){
                if(notFound(r-1,c+1,visited)){
                    BogglePiece piece = new BogglePiece(board[r-1][c+1],r-1,c+1);
                    new_row.add(piece);
                }
            }

            if(c+1!=size){
                if(notFound(r,c+1,visited)){
                    BogglePiece piece = new BogglePiece(board[r][c+1],r,c+1);
                    new_row.add(piece);
                }
            }

            if(r!=size-1 && c+1!=size){
                if(notFound(r+1,c+1,visited)){
                    BogglePiece piece = new BogglePiece(board[r+1][c+1],r+1,c+1);
                    new_row.add(piece);
                }
            }

            if(r!=size-1){
                if(notFound(r+1,c,visited)){
                    BogglePiece piece = new BogglePiece(board[r+1][c],r+1,c);
                    new_row.add(piece);
                }
            }

            if(r!=size-1 && c-1!=-1){
                if(notFound(r+1,c-1,visited)){
                    BogglePiece piece = new BogglePiece(board[r+1][c-1],r+1,c-1);
                    new_row.add(piece);
                }
            }

            if(c-1!=-1){
                if(notFound(r,c-1,visited)){
                    BogglePiece piece = new BogglePiece(board[r][c-1],r,c-1);
                    new_row.add(piece);
                }
            }

            if(new_row.size() > 0){
                options.add(new_row);
                nopts.add(new_row.size()); //adds size of new_row
                visited.add(new_row.get(0)); //adds a visited node
            }
            else{
                backtrack = true;
            }
            
            while(nopts.size() >= 3){
                String str = "";
                found = false;

                for(BogglePiece node: visited){     //creates the string str which represents the word to be searched
                    str = str + node.getChar();
                }
                if(t.searchWord(str) == true){      //search thru the trie of str exists
                    for(String word : words)
                        if(word.compareTo(str) == 0){
                            found = true;
                            break;
                        }
                    if(!found){     //inserts the word to valid words if it is not yet inserted
                        words.add(str);
                        count++;
                    }
                }

                if(t.searchPrefix(str) == false || backtrack == true){  //if word is not a prefix, backtrack
                    options.get(options.size()-1).remove(0);    //remove the letter from the candidates/options list
                    nopts.set(nopts.size()-1, options.get(options.size()-1).size()); //set the new size of the row
                    backtrack = false;

                    /*sets a new "top of stack"*/
                    if((int)nopts.get(nopts.size()-1)!=0) visited.set(visited.size()-1,options.get(options.size()-1).get(0));
                    
                    else{
                        while( options.get(options.size()-1).size() == 0 || options.get(options.size()-1).size() == 1){
                            nopts.remove(nopts.size()-1);
                            options.remove(options.size()-1);
                            visited.remove(visited.size()-1);  
                            if(options.size()-1 == -1) break;          
                        }
                        if(options.size()-1 != -1){
                            options.get(options.size()-1).remove(0);
                            visited.set(visited.size()-1,options.get(options.size()-1).get(0));
                        } 
                    } 
                }
                else break; //breaks out of the loop if a prefix is found; continously add to the current string
            }
        } 
        Collections.sort(words, stringcompare); 
	}
	
	public static boolean notFound(int r, int c, ArrayList<BogglePiece> visited){   //function that checks if a letter is already visited
        for (BogglePiece node: visited){
            if(node.getRowIndex() == r && node.getColIndex() == c) return false;
        }
        return true;
    }
}

class StringCompare implements Comparator<String> {
    public int compare(String s1, String s2){
        if (s1.length() > s2.length())  return -1;
        else if (s1.length() < s2.length()) return 1;
         
        return s1.compareTo(s2);
    }
}