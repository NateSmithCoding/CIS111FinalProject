import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class DeckControl {

	public static void controlMethod() throws IOException{
		// TODO Auto-generated method stub
		
		
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
				QuizMain.quitDialog();
			}
		}
		while(!quit);
	}
	
	//Deck creation method
	public static void createDeck() throws IOException{
		
		String deckName = QuizMain.inputString("Enter a deck name");
		Card[] cardArr = new Card[0];
		
		do
		{
			Card userCard = createCard();
			
			cardArr = QuizMain.addCard(cardArr, userCard);
		}while(QuizMain.confirmDialog("Add another card?"));
		
		Deck userdeck = new Deck(deckName,  cardArr);
		userdeck.createTxt();
	}
	
	//Deck deleting method
	public static void deleteDeck() throws IOException
	{
		
		
		String[] deckArr = QuizMain.getDeckNames();
		QuizMain.sortStrings(deckArr);
		String deleteChoice = QuizMain.dropdownString("Choose a deck to delete", deckArr);
		
		String[] deckTemp = new String[deckArr.length-1];
		int j = 0;
		for(int i = 0; i < deckArr.length; i++)
		{
			
			if(!deckArr[i].equals(deleteChoice))
			{	
				deckTemp[j++] = deckArr[i];
				
				System.out.println(deckTemp[j-1]);
			
			}
			
		}
		//Changing Deck_Names.txt
		File nameFile = new File("Deck_Names.txt");
	      nameFile.createNewFile();
	      
	      PrintWriter namePrint = new PrintWriter(nameFile);
	      
	      for(String str: deckTemp)
	      {
	    	  namePrint.println(str);
	      }
		
	    namePrint.close();
		
	    //Delete deck file  
	    File deleteFile = new File("Decks/"+deleteChoice+".deck");
		
		deleteFile.delete();
		
	}
	
	//Deck edit method 
	public static void editDeck() throws IOException
	{
			
		String[] deckArr = QuizMain.getDeckNames();
		QuizMain.sortStrings(deckArr);
		String deckChoice = QuizMain.dropdownString("Choose a deck to edit", deckArr);	
		File editFile = new File("Decks/" + deckChoice + ".deck");
		Deck editDeck = QuizMain.getDeckFromFile(editFile);
		String[] addRemove = {"Add Card" , "Remove Card"};
		QuizMain.displayString(editDeck.toString());
		String editChoice = QuizMain.dropdownString("Add or Remove" ,addRemove);
		controlCard(editDeck, editChoice);	
		
	}	
	
	//Card control method
	public static void controlCard(Deck deck, String editChoice) throws IOException
	{
		
		if(editChoice.equals("Remove Card"))
		{
			int cardRemove = QuizMain.inputNumber(deck.toString());
			deck.removeCardIndex(cardRemove-1);
			deck.createTxt();
		}
		else{
			
			Card newCard = createCard();
			deck.addCard(newCard);
			deck.createTxt();
			
		}
		
		
			
			
		
		
		
	}
	
	public static Card createCard()
	{
		Card newCard = new Card("","");
		do{
			newCard = new Card(QuizMain.inputString("Enter Question"),
								   (QuizMain.inputString("Enter Answer")));
			if(!noBar(newCard))
			{
				QuizMain.displayString("The '|' character is not allowed.");
			}
		}while(!noBar(newCard));
		return newCard;
	}
	
	public static boolean noBar(Card newCard) 
	{
		String q = newCard.getQuestion();
		String a = newCard.getAnswer();
		
		for(char c : q.toCharArray())
		{
			if(c == '|')
			{
				return false;
			}
		}
		
		for(char c : a.toCharArray())
		{
			if(c == '|')
			{
				return false;
			}
		}
		return true;
	}
}
