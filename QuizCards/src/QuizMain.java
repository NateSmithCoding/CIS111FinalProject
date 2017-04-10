import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class QuizMain
{
   public static void main(String[] args) throws IOException
   {
      	  
      //Make deck from txt
      File decks = new File("Decks");
         
      // create
      boolean bool = decks.mkdir();
      
      // print
      System.out.println("Directory created? "+bool);
      
      String deckFilename = "Decks/";
      File folder = new File(deckFilename); 
      
      
      //list all files
      File[] files = folder.listFiles();      
      
      Deck[] allDecks =  new Deck[(files.length)];
      allDecks = turnFilesToDecks(files);
      
      printDeck(allDecks[3]);

      //addDeck(allDecks, makeMultiDeck());
      
      //printDecks(allDecks);
      
      //Set up study session
      Card[] currentCards = {};
      //Ask user what deck they would like to use and add it to
      
      boolean addDeck = true;
      
      while(addDeck)
      {
         String userDeck = inputString("Enter Deck name");
         if (Deck.checkForDeck(allDecks,userDeck))
         {
            Card[] temp = (Deck.findDeck(allDecks,userDeck)).getCards();
            currentCards = addCards(currentCards,temp);//make large card array
         }
         else
         {
            System.out.println("Error, deck not found");
         }
         if(inputYesOrNo("Add another deck (y/n)").equalsIgnoreCase("n"))
         {
            addDeck= false;
         }   
      }
      
      Deck currentDeck =  new Deck("Current Deck",currentCards);
      System.out.print(currentDeck.toString());
      
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
      return temp;
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
      Deck multiDeck = new Deck("Times_Tables_" + multiRangeLow + "X" + multiRangeHigh,multiplyCards);
      return multiDeck;
   }
   
   
   public static Deck[] turnFilesToDecks(File[] files) throws IOException
   {
      int i = 0;
      Deck[] tempDecks = new Deck[files.length];
      for(File X : files)
      {
    	  tempDecks[i++] = getDeckFromFile(X);
      }
      return tempDecks;
   }
   
   public static void addDeck(Deck[] decks,Deck addDeck)
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
   
   public static void printDeck(Deck deck)
   {
	   System.out.println(deck.toString());
   }
   
   
   public static void printDecks(Deck[] decks)
   {
	      for(Deck X : decks)
	      {
	    	  printDeck(X);
	      }
   }
   
   public static Deck getDeckFromFile(File tempFile) throws IOException
   {
      Scanner scanFileLines = new Scanner(tempFile);
      int line = 0;           
      int totalLines = -1;
      // scan file to get deck
      
      while(scanFileLines.hasNext())
      {
         scanFileLines.nextLine();
         totalLines++;
      }
      
      Scanner scanFile = new Scanner(tempFile);
      Card[] cards = new Card[totalLines];
      String title = "";
      boolean firstline = true;
      while(scanFile.hasNext())
      {
         String nextLine = scanFile.nextLine();
         if(firstline)
         {
            title = nextLine;
            firstline = false;
         }
         else
         {
            Card newCard = new Card(getQuestionFromLine(nextLine),getAnswerFromLine(nextLine));
            //make a card from the text document
            cards[line++] = newCard; //put that card into an array
         }
      }
      
      scanFile.close();
      scanFileLines.close();
      Deck returnDeck = new Deck(title,cards);
      return returnDeck;

   }
   public static boolean isNumeric(String num)
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
   
   public static int inputNumber(String context)
   {
	  String inputNum="";
      do{//check input in range
         inputNum = JOptionPane.showInputDialog(context);
   	  	if(inputNum==null)
   	  	{
   		   System.exit(0);
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
   
   public static String inputString(String context)
   {
      String output = JOptionPane.showInputDialog(context);
      if(output == null)
      {
    	  System.exit(0);
      }
      return output;
   }
   
   public static String inputYesOrNo(String context)
   {
      String answer = "";
      boolean valid = false;
      do
      {
         answer = inputString(context);
         answer = answer.toLowerCase();
         char a = answer.charAt(0);
         if(a == 'y' || a== 'n')
         {
            valid = true;
         }
         else
         {
            JOptionPane.showMessageDialog(null,"Please only enter yes or no.");
         }
      } while (!valid);
      return(answer.substring(0,1));
   }
   
   public static String getQuestionFromLine(String line)
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
   
   public static String getAnswerFromLine(String line)
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
   
   
   public static int combinations(int n)
   {
	   return (n*(n+1))/2;
   }
}