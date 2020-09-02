// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  var id_token = googleUser.getAuthResponse().id_token;
  return fetch("/login", {method: "POST", body: id_token}).then((resp) => {
        if (resp.ok){
            window.location.href = "dashboard.html";
        } else {
            alert("Error has occured");
        }
    });
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}

function getDescription(object) {
    if (object.entity.propertyMap.description == "") {
        return "";
    }
    return "Description: " + capitalizeFLetter(object.entity.propertyMap.description);
}

function capitalizeFLetter(string) { 
    if (string == "") {
        return "Missing Input";
    }
    return string.replace(/^./, string[0].toUpperCase()); 
}

function deleteFromDatastore(object) {
    var keyData = JSON.stringify({"kind": object.entity.key.kind, "id": object.entity.key.id});
    fetch("/delete", {method: "POST", headers: new Headers({ID_TOKEN}), body: keyData}).then((resp) => {
        if (resp.ok){
            location.reload();
        } else {
            alert("Error has occured");
        }
    });
}