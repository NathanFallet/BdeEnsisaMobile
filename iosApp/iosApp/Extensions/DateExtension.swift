//
//  DateExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension Date {
    
    // Constants
    
    static var oneYear: Date {
        Calendar.current.nextDate(
            after: Date(),
            matching: DateComponents(month: 8, day: 31),
            matchingPolicy: .nextTime
        ) ?? Date()
    }
    
    static var fiveYears: Date {
        Calendar.current.date(byAdding: .year, value: 4, to: oneYear) ?? Date()
    }
    
    // String for date
    
    var asString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.string(from: self)
    }
    
}
