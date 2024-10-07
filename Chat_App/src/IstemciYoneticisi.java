import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IstemciYoneticisi implements Runnable {

	private Socket socket ;
	private BufferedReader reader;
	private PrintWriter writer;
	
	
	public IstemciYoneticisi(Socket socket) {
		this.socket = socket;
		try{
			// İstemci ile iletişim için giriş ve çıkış akışlarını açıyoruz
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream() , true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String mesaj ; 
		try {
			while ((mesaj = reader.readLine()) != null ) {
				System.out.println("İstemciden gelen mesaj : " + mesaj);
				SohbetSunucusu.mesajYay(mesaj, this); // Buradaki this şuan üzerinde bulunduğu sınıfın o anki
													  // nesnesini işaret eder onu gönderiyoruz parametre olarak kısaca
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				socket.close();
				SohbetSunucusu.istemciKaldir(this);// İstemci bağlantısı kapandığında sunucudan çıkar
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Sunucu üzerinden istemciye mesaj göndermek için kullanılan metot
	public void mesajGonder(String mesaj) {
		writer.println(mesaj);
	}
}

