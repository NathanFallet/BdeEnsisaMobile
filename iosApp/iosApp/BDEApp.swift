//
//  BDEApp.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Firebase
import FirebaseMessaging
import shared

@main
struct BDEApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
	var body: some Scene {
		WindowGroup {
			RootView()
		}
	}
    
}

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        
        Messaging.messaging().subscribe(toTopic: "broadcast")
        messaging(updateSubscriptionForTopic: "events")
        
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
        )
        application.registerForRemoteNotifications()
        
        return true
    }
    
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .badge, .sound])
    }
    
    func messaging(
        _ messaging: Messaging,
        didReceiveRegistrationToken fcmToken: String?
    ) {
        guard let fcmToken else {
            return
        }
        StorageService.userDefaults?.set(fcmToken, forKey: "fcmToken")
        StorageService.userDefaults?.synchronize()
        guard let token = StorageService.keychain.value(forKey: "token") as? String else {
            return
        }
        Task {
            try await APIService.shared.sendNotificationToken(
                token: token,
                notificationToken: fcmToken
            )
        }
    }
    
    func messaging(
        updateSubscriptionForTopic topic: String
    ) {
        if StorageService.userDefaults?.value(forKey: "notifications_\(topic)") as? Bool ?? true {
            Messaging.messaging().subscribe(toTopic: topic)
        } else {
            Messaging.messaging().unsubscribe(fromTopic: topic)
        }
    }
    
}
