import java.io.*;
import java.util.*;

public class Deck
{
   private String name;
   private Card[] cards;
   
   public Deck(String name,Card[] cards) throws IOException
   {
      this.name  =  name;
      this.cards = cards;
      createTxt();
   }
   
   public static Deck findDeck(Deck[] decks,String name)
   {
      for (Deck d : decks)
      {
         if(d.getName().equals(name))
         {
            return d;
         }
      }
      return decks[0];//never should return
   }
   
   
   public static boolean checkForDeck(Deck[] decks,String name)
   {
      for (Deck d : decks)
      {
         if(d.getName().equals(name))
         {
            return true;
         }
      }
      return false;
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
      int i=0;
      for (Card c : cards)
      {
         output += i + " ";
         output += c.getQuestion() + " ";
         output += c.getAnswer() + "\n";
         i++;
      }
      return output;
   }
   
   public void suffle()
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
   
   public void addCard(Card c) throws IOException
   {
      Card[] temp = new Card[cards.length+1];
      int i=0;
      for (Card x : cards)
      {
         temp[i++] = x; 
      }
      temp[temp.length-1] = c;
      cards =  temp.clone();
      createTxt();
   }

   public void removeCardIndex(int num) throws IOException
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
      createTxt();
   }
   
   public void removeCardQuestion(String question)
   {
      Card[] temp = new Card[cards.length-1];
      int i=0;
      for (Card x : cards)
      {
	      String xQ = x.getQuestion();
	      if(question.equals(xQ))
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
	      
	  File file = new File(deckFilename);
	  file.createNewFile();
      PrintWriter outputFile =  new PrintWriter(file);
      outputFile.println(name);
  
      for (Card c : cards)
      {
    	  outputFile.print(c.getQuestion());
    	  outputFile.print("|");
    	  outputFile.println(c.getAnswer());
      }
      outputFile.close();
   }

}