package it.paleocapa.mastroiannim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
public class JavaBarBot extends TelegramLongPollingBot {

	private static final Logger LOG = LoggerFactory.getLogger(JavaBarBot.class);

	private String botUsername;
	private static String botToken;
	private static JavaBarBot instance;

    //variabili per il bar 
    HashMap<String, Double> listaBar = new HashMap<String, Double>();

	public static JavaBarBot getJavaBossBotInstance(String botUsername, String botToken){
		if(instance == null) {
			instance = new JavaBarBot();
			instance.botUsername = botUsername;
			JavaBarBot.botToken = botToken;
		}
		return instance;
	}

	private JavaBarBot(){
		super(botToken);
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	
	@Override
	public String getBotUsername() {
		return botUsername;
	}

	
	@Override
    public void onUpdateReceived(Update update) {
        String msg = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        // Create a map to store the available products and their prices
        Map<String, Double> listaBar = new HashMap<>();
        listaBar.put("Piadina", 3.0);
        listaBar.put("Patatine", 1.5);
        listaBar.put("CocaCola", 2.0);
        listaBar.put("Panino", 2.5);

        // Create a list to store the ordered products
        List<String> orderList = new ArrayList<>();

        // Check the user's message
        if (msg.equals("/menu")) {
            StringBuilder menu = new StringBuilder("Menu:\n");
            for (String product : listaBar.keySet()) {
                menu.append(product).append(": $").append(listaBar.get(product)).append("\n");
            }
            sendMessage.setText(menu.toString());
        } else if (msg.equals("/order")) {
            orderList.clear(); // Clear any previous order
            sendMessage.setText("What would you like to order?");
        } else if (msg.equals("/stop")) {
            StringBuilder orderSummary = new StringBuilder("Your order:\n");
            double totalCost = 0.0;
        
            for (String product : orderList) {
                orderSummary.append(product).append(": $").append(listaBar.get(product)).append("\n");
                totalCost += listaBar.get(product);
            }
        
            orderSummary.append("Total cost: $").append(totalCost);
            sendMessage.setText(orderSummary.toString());
        }
         else if (listaBar.containsKey(msg)) {
            orderList.add(msg);
            //========================
            sendMessage.setText("What else would you like to order?");
        } else if(msg.equals("/start")) {
            sendMessage.setText("Hi, how can I help you?");
        }else {
            sendMessage.setText("Sorry, I didn't understand your request. Please try again.");
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
