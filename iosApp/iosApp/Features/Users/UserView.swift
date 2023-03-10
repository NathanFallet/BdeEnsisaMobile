//
//  UserView.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct UserView: View {
    
    @EnvironmentObject var rootViewModel: RootViewModel
    @StateObject var viewModel: UserViewModel
    
    var body: some View {
        Form {
            if viewModel.editing {
                Section(header: Text("Photo d'identité")) {
                    HStack {
                        if let image = viewModel.image {
                            Image(uiImage: image)
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 44, height: 45)
                                .clipShape(Circle())
                        }
                        Button(viewModel.image != nil ? "Modifier la photo" : "Ajouter une photo", action: viewModel.showImagePicker)
                    }
                }
            }
            Section(header: Text("Informations")) {
                if viewModel.editing {
                    TextField("Prénom", text: $viewModel.firstName)
                    TextField("Nom", text: $viewModel.lastName)
                    Picker("Année", selection: $viewModel.year) {
                        Text("1A").tag("1A")
                        Text("2A").tag("2A")
                        Text("3A").tag("3A")
                        Text("4A et plus").tag("other")
                        Text("CPB").tag("CPB")
                    }
                    Picker("Option", selection: $viewModel.option) {
                        Text("Informatique et Réseaux").tag("ir")
                        Text("Automatique et Systèmes embarqués").tag("ase")
                        Text("Mécanique").tag("meca")
                        Text("Textile et Fibres").tag("tf")
                        Text("Génie Industriel").tag("gi")
                    }
                    Button("Enregistrer") {
                        viewModel.updateInfo(token: rootViewModel.token) { newUser in
                            if viewModel.isMyAccount {
                                rootViewModel.user = newUser
                            }
                        }
                    }
                } else {
                    HStack {
                        if let image = viewModel.image {
                            Image(uiImage: image)
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 44, height: 45)
                                .clipShape(Circle())
                        }
                        VStack(alignment: .leading, spacing: 2) {
                            Text("\(viewModel.firstName) \(viewModel.lastName)")
                            Text(viewModel.user.description_)
                        }
                    }
                }
            }
            if !viewModel.isMyAccount {
                Section(header: Text("Cotisation")) {
                    Text(viewModel.user.cotisant != nil ? "Cotisant" : "Non cotisant")
                        .foregroundColor(viewModel.user.cotisant != nil ? Color.green : Color.red)
                    if let cotisant = viewModel.user.cotisant, !viewModel.editing {
                        Text("Expire : \(cotisant.expiration.renderedDate)")
                    }
                    if viewModel.editing {
                        DatePicker(
                            "Expire",
                            selection: $viewModel.expiration,
                            displayedComponents: .date
                        )
                        Button("1 an") {
                            viewModel.expiration = .oneYear
                        }
                        Button("Scolarité") {
                            viewModel.expiration = .fiveYears
                        }
                        Button("Enregistrer") {
                            viewModel.updateExpiration(token: rootViewModel.token)
                        }
                    }
                }
            }
        }
        .navigationTitle(Text("Utilisateur"))
        .toolbar {
            if viewModel.editable {
                Button(viewModel.editing ? "Terminé" : "Modifier", action: viewModel.toggleEdit)
            }
        }
        .sheet(isPresented: $viewModel.imagePickerShown) {
            ImagePicker { image in
                viewModel.updateImage(token: rootViewModel.token, image: image)
            }
        }
        .onAppear {
            viewModel.onAppear(token: rootViewModel.token)
        }
    }
    
}
