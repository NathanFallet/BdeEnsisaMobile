//
//  FeedViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class FeedViewModel: ObservableObject {
    
    @Published var events = [Event]()
    
    func onAppear() {
        fetchEvents()
    }
    
    func fetchEvents() {
        Task {
            let events = try await APIService.shared.getEvents()
            DispatchQueue.main.async {
                self.events = events
                self.events.append(Event(id: "abc", title: "Event", content: "Coucou", start: Kotlinx_datetimeInstant.companion.parse(isoString: "2023-01-01T12:00:00.000Z"), end: Kotlinx_datetimeInstant.companion.parse(isoString: "2023-01-01T12:00:00.000Z"), topicId: "", topic: nil))
            }
        }
    }
    
}
