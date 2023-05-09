package it.paleocapa.mastroiannim;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
public class JavaBossBot extends TelegramLongPollingBot {

	private static final Logger LOG = LoggerFactory.getLogger(JavaBossBot.class);

	private String botUsername;
	private static String botToken;
	private static JavaBossBot instance;

	public static JavaBossBot getJavaBossBotInstance(String botUsername, String botToken){
		if(instance == null) {
			instance = new JavaBossBot();
			instance.botUsername = botUsername;
			JavaBossBot.botToken = botToken;
		}
		return instance;
	}

	private JavaBossBot(){
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
		//update è l'input dell'utente
		String msg = update.getMessage().getText();
		String chatId=update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        
        
        LinkedList<String> prodotti = new LinkedList<String>();
        prodotti.add("piadina");
        prodotti.add("panino");
        prodotti.add("CocaCola");
        
        boolean stoOrdinando = false;
        LinkedList <String> list = new LinkedList<String>();
        if(msg.equals("/menu")){
            sendMessage.setText(
                "menu:\n piadina $2\n panino $1.5\n CocaCola $1"
            );
        }else if(msg.equals("/order")){
            stoOrdinando=true;
            sendMessage.setText("cosa vuoi odrinare?");
        }else if(msg.equals("/stop")){
            stoOrdinando=false;
            sendMessage.setText(list.toString());
        }else if(prodotti.contains(msg)){
            list.add(msg);
            sendMessage.setText("poi?");
        }

        
        /*SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(msg);*/
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
           // gestione errore in invio
           e.printStackTrace();
        }
	}
}
