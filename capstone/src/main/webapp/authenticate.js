var CLIENT_ID = config.CLIENT_ID;
var API_KEY = config.API_KEY;
var DISCOVERY_DOCS = ["https://script.googleapis.com/$discovery/rest?version=v1"];
var SCOPES = "https://www.googleapis.com/auth/forms https://www.googleapis.com/auth/youtube";
var ID_TOKEN;

var [dynamicButton, signoutButton] = createDynamicSigninButton();
document.currentScript.after(createAPIScriptElement());
window.addEventListener('load', handleClientLoad);

/**
*  Create login and logout buttons to use
*/
function createDynamicSigninButton() {
    var dynamicButton = document.createElement("div");
    var logoutButton = dynamicButton.appendChild(createLogoutButton());
    return [dynamicButton, logoutButton];
}

function createLogoutButton(){
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
            <button type="button" class="btn btn-default" id="signout_button">Sign Out</button>
            `, "text/html");
    return doc.body;
}

function createAPIScriptElement() {
    const scriptElement = document.createElement('script');
    scriptElement.async = true;
    scriptElement.defer = true;
    scriptElement.src = "https://apis.google.com/js/api.js";
    return scriptElement;
}

/**
*  On load, called to load the auth2 library and API client library.
*/
function handleClientLoad() {
    gapi.load('client:auth2', initClient);
}

/**
*  Initializes the API client library and sets up sign-in state
*  listeners.
*/
function initClient() {
    gapi.client.init({
        apiKey: API_KEY,
        clientId: CLIENT_ID,
        discoveryDocs: DISCOVERY_DOCS,
        scope: SCOPES
    }).then(function () {
        // Set ID_TOKEN and dispatch event
        ID_TOKEN = gapi.auth2.getAuthInstance().currentUser.get().getAuthResponse().id_token;
        window.dispatchEvent(new Event('authorized'));

        // Sign in and listen for sign-in state changes.
        gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
        signoutButton.onclick = gapi.auth2.getAuthInstance().signOut;
    }, function(error) {
        console.log("Error has occured: " + error);
    });
}

/**
*  Called when the signed in status changes, to update the UI
*  appropriately.
*/
function updateSigninStatus(isSignedIn) {
    if (!isSignedIn) {
        ID_TOKEN = null;
        window.location.href = "index.html";
    }
}