import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class QuizMain
{
   public static void main(String[] args) throws IOException
   {
	  
      //Make deck directory 
      File deckDir = new File("Decks");
         
      // create decks folder
      deckDir.mkdir();
      
      DeckControl.controlMethod();
           
   }
   
   //user chooses decks and then the questions are asked
   public static void studySession() throws IOException
   {
	
	  //get an array from deck name text file
	  String[] allDecksNames = getDeckNames();
	  
	  //sort deck names alphabetically
	  sortStrings(allDecksNames);
	  
	  //STUDY SESSION BELOW
	  Deck currentDeck = createStudyDeck(allDecksNames);
	  
	  if(!currentDeck.getName().equals("cancel"))
	  {
		  
		  int repeatTimes = 0;
		  do
		  {
			  repeatTimes = inputNumber("How many times should each question be asked?");
			  if(repeatTimes<1)
			  {
				  displayString("Error, need to go through at least once.");
			  }
		  } while(repeatTimes<1);
		  
		  Card[] tempCards = currentDeck.getCards();
		  for(int i=1;i<repeatTimes;i++)
		  {
			  currentDeck.addCards(tempCards);
		  }
		  
		  currentDeck.shuffle();//shuffle deck
		  
		  int cardsLeft=currentDeck.length()-1;
		  for(int k=0;k<currentDeck.length();k++)//k is the times 
		  {
			  Card[] studyCards = currentDeck.getCards();
			  int quizInt=0;
			  do{//question loop
		    	  quizInt = quizCardDialog(studyCards[k].getQuestion()+"\nQuestions left: " + cardsLeft,"Show Answer");
		    	  if(quizInt==1)
		    	  {
		    		  displayString("Question Flagged ");
		    		  currentDeck.addCardRandomIndex(studyCards[k],k);
		    		  cardsLeft++;
		    	  }
		    	  else if(quizInt == 2)
		    	  {
		    		  if(quitDialog())
		    		  {
		    			  k=9999;
		    			  DeckControl.controlMethod();
		    		  }
		    	  }
			  }while(quizInt!=0);
			  cardsLeft--;
			  do{//answer loop
		    	  quizInt = quizCardDialog(studyCards[k].getAnswer(), "Next Question");
		    	  if(quizInt==1)
		    	  {
		    		  displayString("Question Flagged");
		    		  currentDeck.addCardRandomIndex(studyCards[k],k);
		    		  cardsLeft++;
		    	  }
		    	  else if(quizInt == 2)
		    	  {
		    		  if(quitDialog())
		    		  {
		    			  k=9999;
		    			  DeckControl.controlMethod();
		    		  }
		    	  }
			  }while(quizInt !=0);
		  }
	  }
   }
   
   //sorts strings in alpha 
   public static void sortStrings( String[] strings )
   {
     int j;
     boolean flag = true;   // set flag to true to begin first pass
     String temp;   //holding variable

     while ( flag )
     {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < strings.length -1;  j++ )
            {
                   if ( alphaCompare(strings[j+1],strings[j]) )
                   {
                          temp = strings[ j ];                //swap elements
                          strings[ j ] = strings[ j+1 ];
                          strings[ j+1 ] = temp;
                          flag = true;              //shows a swap occurred  
                  } 
            } 
      } 
   } 
   
   //compares strings alphabetically
   public static boolean alphaCompare(String stringA, String stringB)//returns true if a is alphabetically before b
   {
	   
	   int stringLen = Math.min(stringA.length(), stringB.length());
	   
	   for(int i=0;i<stringLen;i++)
	   {
		   char a = stringA.charAt(i);
		   char b = stringB.charAt(i);
		   if(a<b)
		   {
			   return true;
		   }
		   else if(a>b)
		   {
			   return false;
		   }
	   }
	   return stringA.length()-stringB.length()<0;
   }

   //reads "deck_names" file and returns the string array of there names
   public static String[] getDeckNames() throws IOException 
   {
      //get the text file with all the deck names
      File deckNames = new File("Deck_Names.txt");//load name file
      deckNames.createNewFile();
      //find number of lines
      Scanner scanNamesLines = new Scanner(deckNames);//scan lines
      int k=0;
      while(scanNamesLines.hasNext())
      {
    	  k++;
    	  scanNamesLines.nextLine();
      }
    	 
      scanNamesLines.close();
      //Scan deck name file and get names
      String[] allDecksNames = new String[k];//create array with k size
      k=0;//reset k for while loop
      Scanner scanNames = new Scanner(deckNames);//scan lines
      while(scanNames.hasNext())
      {
    	  allDecksNames[k++] = scanNames.nextLine();//filling the array with text file
      }
      scanNames.close();
      
      return allDecksNames;
   }
   
   //quick way to do Yes No Joption pane
   public static boolean confirmDialog(String display)
   {
	   return (0==JOptionPane.showOptionDialog(null, display, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null));
   }
   
   //JoptionPane with 3 options(for the quiz cards)
   public static int quizCardDialog(String display,String option1)
   {
	   Object[] options1 = { option1, "Flag","Quit" };
	   return JOptionPane.showOptionDialog(null, display, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);
   }
   
   
   
   public static void displayString(String display)
   {
	   JOptionPane.showMessageDialog(null, display);
   }
   
   public static boolean quitDialog()
   {
	   return confirmDialog("Are you sure you want to quit");
   }
   
   public static Deck createStudyDeck(String[] deckNams) throws IOException
   {
	  Card[] currentCards = {};
	  
	  
      boolean addDeck = true;
      
      boolean oneAdded = false;
      
      while(addDeck)
      {
         String userDeck = dropdownString("Choose a deck:",deckNams);
         
         boolean cancel = userDeck.equals("cancel");
         
         
         if(!cancel)
         {
        	File tempFile = new File("Decks/"+ userDeck + ".deck");
         
         	Deck tempDeck = getDeckFromFile(tempFile);
         
         	Card[] tempCards = tempDeck.getCards();
         
         	currentCards = addCards(currentCards, tempCards);
         	
         	oneAdded = true;
         }
         
         if(oneAdded)
         {
	         if(!confirmDialog("Add another deck?"))
	         {
	            addDeck= false;
	         }
         }
         else if(cancel)
         {
        	 return new Deck("cancel",null);
         }
      }
      return new Deck("Current Deck",currentCards);
   }
   
   public static String dropdownString(String context,String[] options) 
   {
		JComboBox comboBox = new JComboBox(options);
		boolean canceled = false;
		do{
			canceled = 2==JOptionPane.showConfirmDialog(null, comboBox, context, JOptionPane.OK_CANCEL_OPTION);
			if(canceled)
			{
				return "cancel";
			}
		} while(canceled);
		return options[comboBox.getSelectedIndex()];
   }

   public static Card[] addCard(Card[] cards, Card card)
   {
      Card[] temp = new Card[cards.length+1];
      int i = 0;
      for(Card c : cards)
      {
         temp[i++] = c;
      }
      temp[temp.length-1] = card;
      return temp.clone();
   }
   
   public static Card[] addCards(Card[] cards, Card[] addCards)
   {
      Card[] temp = new Card[cards.length+addCards.length];
      int i = 0;
      for(Card c : cards)
      {
         temp[i++] = c;
      }
      for(Card d : addCards)
      {
         temp[i++] = d;
      }
      return temp;
   }
   
   //used to create a times table deck for testing deck sizes
   public static Deck makeMultiDeck() throws IOException
   {
      //create times table deck
	  int multiRangeHigh = 0;
	  int multiRangeLow  = 0;
	  int multiDiff      = 0;
      do
      {
    	  multiRangeHigh = inputNumber("Highest number to have in times tables");
    	  multiRangeLow  = inputNumber("Lowest number to have in times tables");
    	  multiDiff      = multiRangeHigh-multiRangeLow;
      } while(multiDiff<=0||multiDiff>=20000);
      
      int multiSize = (combinations(multiDiff+1));
      
      Card[] multiplyCards =  new Card[multiSize];
      int i=0;
      
      for(int j=multiRangeLow;j<=multiRangeHigh;j++)
      {
		for(int k=j;k<=multiRangeHigh;k++)
		{
			String question = j+" * " + k;
			Card temp =  new Card(question,j*k+"");
			System.out.println(question);
			multiplyCards[i++] = temp;
		}
      }
      Deck multiDeck = new Deck("Times Tables " + multiRangeLow + "X" + multiRangeHigh,multiplyCards);
      return multiDeck;
   }
   
   
   public static Deck[] turnFilesToDecks(File[] files) throws IOException //old method for creating deck arrays
   {
      int i = 0;
      Deck[] tempDecks = new Deck[files.length];
      for(File X : files)
      {
    	  tempDecks[i++] = getDeckFromFile(X);
      }
      return tempDecks;
   }
   
   public static String deckNamesToString(Deck[] decks) //old method for test deck arrays
   {
	   String output = "";
	   int i=1;
	   for(Deck d : decks)
	   {
		   output += i++ + ": ";
		   output += d.getName() + "\n";
	   }
	   return output;
   }
   
   public static void addDeck(Deck[] decks,Deck addDeck)//old method for adding decks
   {
      Deck[] temp = new Deck[decks.length+1];
      int i=0;
      for (Deck d : decks)
      {
         temp[i++] = d; 
      }
      temp[temp.length-1] = addDeck;
      decks =  temp.clone();
   }
   
   public static void printDeck(Deck deck) //for testing decks
   {
	   System.out.println(deck.toString());
   }
   
   
   public static void printDecks(Deck[] decks)//old method for testing deck arrays
   {
	      for(Deck X : decks)
	      {
	    	  printDeck(X);
	      }
   }
   
   public static Deck getDeckFromFile(File tempFile) throws IOException//turns a file into a deck by reading it
   {
      Scanner scanFileLines = new Scanner(tempFile);
      int line = 0;           
      int totalLines = 0;
      // scan file to get deck
      
      while(scanFileLines.hasNext())
      {
         scanFileLines.nextLine();
         totalLines++;
      }
      
      Scanner scanFile = new Scanner(tempFile);
      Card[] cards = new Card[totalLines];
      String title = tempFile.getName();
      title = title.substring(0,title.length()-5);
      while(scanFile.hasNext())
      {
    	  
		  String nextLine = scanFile.nextLine();
		  Card newCard = new Card(getQuestionFromLine(nextLine),getAnswerFromLine(nextLine));
		  //make a card from the text document
		  cards[line++] = newCard; //put that card into an array
      }
      
      scanFile.close();
      scanFileLines.close();
      Deck returnDeck = new Deck(title,cards);
      return returnDeck;

   }
   public static boolean isNumeric(String num)//checks if string is numeric
   {
	  if(num==null)
	  {
		  System.exit(0);
	  }
	  
      try
      {
         Integer.parseInt(num);
         return true;
      }
      catch (NumberFormatException e)
      {
         return false;
      }
   }
   
   public static int inputNumber(String context)//auto turns string into an int
   {
	  String inputNum="";
      do{//check input in range
         inputNum = JOptionPane.showInputDialog(context,null);
   	  	if(inputNum==null)
   	  	{
   	  		if(quitDialog())
   	  		{
   	  			System.exit(0);
   	  		}
   	  	}
        if( isNumeric(inputNum))
        {
           return Integer.parseInt(inputNum);
        }
        else
        {
           JOptionPane.showMessageDialog(null,"ERROR not a number");
        }
      } while(!isNumeric(inputNum));
      
      return 0;
   }
   
   public static String inputString(String context)//shorter input dialog
   {
	  
	  String output = JOptionPane.showInputDialog(context);
	  
	  if(output == null)
      {
    	  return "cancel";
      }
	  
	  while(output.equals(""))
	  {
		  output = JOptionPane.showInputDialog(context);
	  } 
      
      return output;
   }
   
   
   public static String getQuestionFromLine(String line)//turns line into a substring that is only the question
   {
      boolean ended = false;
      String returnString = "";
      for (char cha : line.toCharArray())
      {
         String chaStr = String.valueOf(cha);
         if (chaStr.equals("|") || ended)
         {
            ended = true;
         }
         else
         {
            returnString += chaStr;
         }
      }
      return returnString;
   }
   
   public static String getAnswerFromLine(String line)//get substring of line that is only the answer
   {
      boolean started = false;
      String returnString = "";
      for (char cha : line.toCharArray())
      {
         String chaStr = String.valueOf(cha);//turn char into string
         if (chaStr.equals("|") && !started)
         {
            started = true;
         }
         else if (started && !( chaStr.equals("|") ))
         {
            returnString += chaStr;
         }
      }
      return returnString;
   }
   
   
   public static int combinations(int n)//gets the size for a multideck
   {
	   return (n*(n+1))/2;
   }

   public static int dropdownInt(String context, String[] options) 
   {
	   JComboBox comboBox = new JComboBox(options);
		boolean canceled = false;
		do{
			canceled = 2==JOptionPane.showConfirmDialog(null, comboBox, context, JOptionPane.OK_CANCEL_OPTION);
			if(canceled)
			{
				return -999;
			}
		} while(canceled);
		return comboBox.getSelectedIndex();
   }
}