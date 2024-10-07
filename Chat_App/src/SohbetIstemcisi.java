import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SohbetIstemcisi {

	private String sunucuAdi;
	private int port;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private BufferedReader konsolReader;
	
	public SohbetIstemcisi(String sunucuAdi , int port) {
		this.sunucuAdi = sunucuAdi;
		this.port = port; 
		
	}
	
	
	public void baslat() {
		try {
			socket = new Socket(sunucuAdi,port); // Sunucuya bağlan
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(),true);
			konsolReader = new BufferedReader(new InputStreamReader(System.in));
			// konsolReader objem konsoldan okuma yapacak
			
			// Sunucudan gelen mesajları okumak için bir thread sınıfı oluşturdum
			new Thread(new GelenOkuyucu()).start();;
			System.out.println("Bağlantı kuruldu lütfen bir mesaj giriniz");
			String mesaj;
            while ((mesaj = konsolReader.readLine()) != null) { // Konsoldan mesaj oku
                writer.println(mesaj); // Mesajı sunucuya gönder
            }

	        
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Bir thread sınıfı oluşturduk
	private class GelenOkuyucu implements Runnable {
		
		@Override
		public void run() {
			String gelenMesaj;
			try {
				while ((gelenMesaj = reader.readLine()) != null) {
					System.out.println("Sunucudan gelen mesaj : " + gelenMesaj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		SohbetIstemcisi istemci = new SohbetIstemcisi("localhost", 1234);
		istemci.baslat();
	}
}
