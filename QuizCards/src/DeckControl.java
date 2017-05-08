import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class DeckControl {

	public static void controlMethod() throws IOException{
		// TODO Auto-generated method stub
		
		//Main menu drop down
  		String[] deckOptions = {"Start Study Session","Create", "Edit", "Delete","Quit"};
  		boolean quit = false;
  		do{
  			
  			String controlChoice = QuizMain.dropdownString("What do you want to do?", deckOptions);
			
  			if(controlChoice.equals("Create"))
			{
					createDeck();
			}
			else if(controlChoice.equals("Edit"))
			{	
				editDeck();
			}
			else if(controlChoice.equals("Delete"))
			{
				deleteDeck();
			}
			else if(controlChoice.equals("Start Study Session"))
			{
				QuizMain.studySession();
			}
			else
			{
				if(QuizMain.quitDialog())
				{
					quit = true;
				}
			}
		}
		while(!quit);
	}
	
	//Deck creation method
	public static void createDeck() throws IOException{
		
		String deckName = QuizMain.inputString("Enter a deck name");
		boolean cancel = deckName.equals("cancel");
		if(!cancel)
		{
			Card[] cardArr = new Card[0];
			
			do
			{
				Card userCard = createCard();
				
				cardArr = QuizMain.addCard(cardArr, userCard);
			}while(QuizMain.confirmDialog("Add another card?"));
			
			Deck userdeck = new Deck(deckName,  cardArr);
			userdeck.createTxt();
		}
	}
	
	//Deck deleting method
	public static void deleteDeck() throws IOException
	{
		
		
		String[] deckArr = QuizMain.getDeckNames();
		QuizMain.sortStrings(deckArr);
		String deleteChoice = QuizMain.dropdownString("Choose a deck to delete", deckArr);
		
		boolean cancel = deleteChoice.equals("cancel");
		
		String spanish = deleteChoice.toLowerCase();
		String context = "Are you sure want to delete?";
		if(spanish.contains("spanish"))
		{
			context = "¿Estas seguro que quieres borrarlo?";
		}
		if(!cancel && QuizMain.confirmDialog(context))
		{
			String[] deckTemp = new String[deckArr.length-1];
			int j = 0;
			for(int i = 0; i < deckArr.length; i++)
			{
				
				if(!deckArr[i].equals(deleteChoice))
				{	
					deckTemp[j++] = deckArr[i];
					
				
				}
				
			}
			//Changing Deck_Names.txt
			File nameFile = new File("Deck_Names.txt");
		      nameFile.createNewFile();
		      
		      PrintWriter namePrint = new PrintWriter(nameFile);
		      
		      String deckrem = "Decks Remaining:\n";
		      
		      for(String str: deckTemp)
		      {
		    	  namePrint.println(str);
		    	  deckrem += str+"\n";
		      }
			
		    QuizMain.displayString(deckrem);
		    namePrint.close();
			
		    
		    //Delete deck file  
		    File deleteFile = new File("Decks/"+deleteChoice+".deck");
			
			deleteFile.delete();
		}
	}
	
	//Deck edit method 
	public static void editDeck() throws IOException
	{
			
		String[] deckArr = QuizMain.getDeckNames();
		QuizMain.sortStrings(deckArr);
		String deckChoice = QuizMain.dropdownString("Choose a deck to edit", deckArr);
		boolean cancel = deckChoice.equals("cancel");
		while(!cancel)
		{
			File editFile = new File("Decks/" + deckChoice + ".deck");
			Deck editDeck = QuizMain.getDeckFromFile(editFile);
			String[] addRemove = {"Add Card" , "Remove Card"};
			String editChoice = QuizMain.dropdownString("Add or Remove" ,addRemove);
			controlCard(editDeck, editChoice);
			if(editChoice.equals("cancel"))
			{
				cancel = QuizMain.confirmDialog("Stop editing?");
			}
		}
	}	
	
	//Card control method
	public static void controlCard(Deck deck, String editChoice) throws IOException
	{
		
		if(editChoice.equals("Remove Card"))
		{
			String[] cardsStrings = cardsToStrings(deck.getCards());
			int cardRemove = QuizMain.dropdownInt("Cards",cardsStrings);
			try{
			deck.removeCardIndex(cardRemove);
			deck.createTxt();
			} catch(Exception e)
			{
				
			}
		}
		else if(editChoice.equals("Add Card"))
		{	
			Card newCard = createCard();
			if(!newCard.getQuestion().equals(""))
			{
				deck.addCard(newCard);
				deck.createTxt();
			}
		}
	}
	
	//Converting cards into strings
	private static String[] cardsToStrings(Card[] cards) 
	{
		String[] output = new String[cards.length];
		int i=0;
		for (Card c : cards)
		{
			output[i++]= i + ": "+ c.getQuestion() +" | "+ c.getAnswer();
		}
		return output;
	}
	
	//Creating cards
	public static Card createCard()
	{
		Card newCard = new Card("","");
		String question;
		String answer;
		boolean noBar;
		do{
			question = QuizMain.inputString("Enter Question");
			noBar = noBar(question);
		}while(!noBar);
		
		do{
			answer   = QuizMain.inputString("Enter Answer");
			noBar = noBar(answer);
		}while(!noBar);
			boolean cancel = (question.equals("cancel")) || (answer.equals("cancel")); 
			
			if(!cancel)
			{
				newCard = new Card(question,answer);
			}
		return newCard;
	}
	
	//Makes sure the user can't use a "|" character as to avoid formatting errors
	public static boolean noBar(String str) 
	{
		if(str.contains("|"))
		{
			QuizMain.displayString("The '|' character is not allowed.");
			return false;
		}
		return true;
	}
}
