package net.mattbenson.notifications;

import java.util.List;

public class NotiRemovalThread implements Runnable {
	private List<Notification> notifications;
	
	public NotiRemovalThread(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	@Override
	public void run() {
		while(true) {
			notifications.removeIf(noti -> noti.isDead());
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
