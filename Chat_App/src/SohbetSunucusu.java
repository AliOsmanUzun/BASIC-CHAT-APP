import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class SohbetSunucusu {

	// Sunucu port üzerinden gelen istemci ( client )  bağlantılarını kabul eder ve her bir istemciyi ayrı bir thread yönetir
	
	private static final int PORT = 1234; // Sunucu port numarası
	
	private static Set<IstemciYoneticisi> istemciIsleyici = new HashSet<IstemciYoneticisi>();
	
	public static void main(String[] args) {
		
		try(ServerSocket sunucuSoketi = new ServerSocket(PORT);){
			// Port üzerinden bir bağlantı oldumu diye kontrol etmek için serversocket kullanıyoruz
			// Belirttiğimiz port üzerinde dinleme yapıyor
			System.out.println("Sunucu çalışıyor. PORT : " + PORT);
			
			while (true) {
				// Sunucu tarafında bir soket oluştur ama ne zaman oluştur
				// yeni bir istemci ( client ) bağlanmak istediğinde dinle portu ve socket oluştur
				Socket soket = sunucuSoketi.accept();
				System.out.println("Yeni istemci ( client ) bağlandı : " + soket.getInetAddress());
				
				// Yeni bir iş parçaçığı oluşturarak her defasında istemciyi yönetiyoruz
				IstemciYoneticisi istemciYoneticisi = new IstemciYoneticisi(soket);
				istemciIsleyici.add(istemciYoneticisi);
				new Thread(istemciYoneticisi).start();
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	// Tüm istemcilere mesaj gönderen bir metot
	public static void mesajYay(String mesaj, IstemciYoneticisi gonderici) {
		for(IstemciYoneticisi istemci : istemciIsleyici) {
			if (istemci != gonderici) {
				istemci.mesajGonder(mesaj);
				
			}
		}
	}
	
	// İstemci bağlantısı kapandığında onu setten çıkaran bir metot
	
	public static void istemciKaldir(IstemciYoneticisi istemciYoneticisi) {
		istemciIsleyici.remove(istemciYoneticisi);
		System.out.println("İstemci bağlantısı kapandı");
	}
	
	
}
