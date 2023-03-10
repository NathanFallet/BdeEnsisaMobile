//
//  ClubView.swift
//  BDE
//
//  Created by Nathan Fallet on 13/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ClubView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: ClubViewModel
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                ClubCard(
                    club: viewModel.club,
                    badgeText: rootViewModel.user?.cotisant != nil && !viewModel.members.contains(where: { $0.userId == rootViewModel.user?.id }) ? "REJOINDRE" : nil,
                    badgeColor: .accentColor,
                    action: { _ in
                        viewModel.join(token: rootViewModel.token)
                    },
                    detailsEnabled: false
                )
                HStack {
                    Text("Membres")
                        .font(.title)
                    Spacer()
                }
                .padding(.top)
                LazyVGrid(
                    columns: [GridItem(.adaptive(minimum: 300, maximum: 400))],
                    alignment: .leading
                ) {
                    ForEach(viewModel.members, id: \.userId) { membership in
                        HStack {
                            VStack(alignment: .leading) {
                                Text("\(membership.user?.firstName ?? "") \(membership.user?.lastName ?? "")")
                                    .fontWeight(.bold)
                                Text(membership.user?.description_ ?? "")
                            }
                            Spacer()
                            Text(membership.role == "admin" ? "ADMIN" : "MEMBRE")
                                .font(.caption)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 6)
                                .foregroundColor(.white)
                                .background(membership.role == "admin" ? Color.black : Color.green)
                                .cornerRadius(8)
                        }
                        .cardView()
                    }
                }
            }
            .padding()
        }
        .onAppear(perform: viewModel.onAppear)
        .navigationTitle(Text(viewModel.club.name))
        .toolbar {
            if viewModel.members.contains(where: { $0.userId == rootViewModel.user?.id }) {
                Button("Quitter") {
                    viewModel.leave(token: rootViewModel.token)
                }
            }
        }
    }
    
}
