package br.com.christianovale.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TesteProdutorTopico {
	
	public static void main(String[] args) throws Exception {
		 
		InitialContext context = new InitialContext();

		//importe do package javax.jms
		ConnectionFactory cf = (ConnectionFactory)context.lookup("ConnectionFactory");
		Connection conexao = cf.createConnection();
		conexao.start();

		Session session = conexao.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination topico = (Destination) context.lookup("loja");
		
		MessageProducer producer = session.createProducer(topico);
		
		Message message = session.createTextMessage("<pedido><id>666</id></pedido>");
		message.setBooleanProperty("ebook", false);//Propriedade para o Selector
		producer.send(message);
		/*for(int i=0 ; i<=100 ; i++){
			
			Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
			producer.send(message);
		}*/
		
		//Msnsagem via Objeto----------------------------
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		Message messagem = session.createObjectMessage(pedido);
		producer.send(messagem);
		//new Scanner(System.in).nextLine();
	
		session.close();
		conexao.close();    
		context.close();
	}


}
