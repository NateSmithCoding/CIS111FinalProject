import java.io.*;
import java.util.*;

public class Deck
{
   private String name;
   private Card[] cards;
   
   public Deck(String name,Card[] cards)
   {
      this.name  =  name;
      this.cards = cards;
   }
   
   public Card[] getCards()
   {
      return cards;
   }
   
   public String getName()
   {
      return name;
   }
   
   public Card getCard(int index)
   {
      return cards[index];
   }
   
   public String toString()
   {
      String output=name + "\n";
      int i=1;
      for (Card c : cards)
      {
         output += i + " ";
         output += c.getQuestion() + " ";
         output += c.getAnswer() + "\n";
         i++;
      }
      return output;
   }
   
   public void shuffle()
   {
      Random rand = new Random();
      for(int i=0; i<cards.length;i++)
      {
         int r = rand.nextInt(cards.length);
         
         Card temp = cards[r];
         cards[r]  = cards[i];
         cards[i]  = temp;
      }
   }
   
   public void addCard(Card c)
   {
      Card[] temp = new Card[cards.length+1];
      int i=0;
      for (Card x : cards)
      {
         temp[i++] = x; 
      }
      temp[temp.length-1] = c;
      cards =  temp.clone();
   }
   
   public void addCards(Card[] addCards)
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
      cards = temp.clone();
   }
   

   public void removeCardIndex(int num)
   {
      Card[] temp = new Card[cards.length-1];
      int i=0;
      int j=0;
      for (Card x : cards)
      {
   	   if(j!=num)
           {
             temp[i++] = x;
           }
   	   j++;
      }
      cards =  temp.clone();
   }
   
   public void removeCardQuestion(String question)
   {
      Card[] temp = new Card[cards.length-1];
      int i=0;
      for (Card x : cards)
      {
	     String xQ = x.getQuestion();
	     if(!question.equals(xQ))
         {
            temp[i++] = x;
         }
      }
      cards =  temp.clone();
   }
   
   public void createTxt() throws IOException
   {
      String deckFilename = "Decks/";
      
      deckFilename += name + ".deck";
	      
	  File deckfile = new File(deckFilename);
	  boolean newdeck = deckfile.createNewFile();//checks if deck is new
	  
	  //make deck file or edit deckfile
      PrintWriter outputFile =  new PrintWriter(deckfile);
      for (Card c : cards)
      {
    	  outputFile.print(c.getQuestion());
    	  outputFile.print("|");
    	  outputFile.println(c.getAnswer());
      }
      outputFile.close();
      
      if(newdeck)//if new deck add deck name to names file
      {
    	  File nameFile = new File("Deck_Names.txt");
    	  nameFile.createNewFile();
      
	      String[] names = QuizMain.getDeckNames();
	      
	      PrintWriter namePrint = new PrintWriter(nameFile);
	      
	      for(String str: names)
	      {
	    	  namePrint.println(str);
	      }
	      namePrint.println(name);
	         
	      namePrint.close();
      }
  
   }

	public int length() 
	{
		return cards.length;
	}

	public void addCardRandomIndex(Card c,int num)//card added and card number
	{
		Random rand = new Random();
		int x = num+1;
		int r = rand.nextInt(cards.length+1-x)+x;
		addCardIndex(c, r);
	}
	
	public void addCardIndex(Card c,int in)
	{
      Card[] temp = new Card[cards.length+1];
      int j=0;
      for (int i=0;i<temp.length;i++)
      {
    	 if(i!=in)
    	 {
    		 temp[i] = cards[j++];
    	 }
    	 else
    	 {
    		 temp[i] = c;
    	 }
      }
      cards =  temp.clone();
	}

}