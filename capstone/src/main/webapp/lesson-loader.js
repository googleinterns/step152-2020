document.getElementById("signout").prepend(dynamicButton);
window.addEventListener('authorized', getLessons);

const FORM = "form";
const VIDEO = "video";
const CONTENT = "content";
const IMAGE = "image";

const queryString = "/lesson?room_id=" + getRoomId() + "&action=" + getJoinStatus();
const videoPath = "https://www.youtube.com/embed/";

function createForm(title, description) {
    var scriptId = config.SCRIPT_ID;

    gapi.client.script.scripts.run({
    'scriptId': scriptId,
    'resource': {
        "function": "createForm",
        "parameters": [],
        "devMode": false
    }
    }).then(function(resp) {
        if (resp.result?.error?.status != null) {
            console.log('Error calling API: ' + result);
        } else if (resp.result?.error != null) {
            console.log("Script error message: " + result.error);
        } else { 
            var formData = JSON.stringify({"type": FORM, "title": title, "description": description, "editUrl": resp.result.response.result.editUrl, "url": resp.result.response.result.url});          
            return fetch(queryString, {method: "POST", headers: new Headers({ID_TOKEN}), body: formData});  
        }
    }).then((resp) => {
        if (resp.ok){
            getLessons();
        } else {
            alert("Error has occured");
        }
    });
}

function createVideo(title, description, url) {
    var videoData = JSON.stringify({"type": VIDEO, "title": title, "description": description, "url": url});
    fetch(queryString, {method: "POST", headers: new Headers({ID_TOKEN}), body: videoData}).then((resp) => {
        if (resp.ok){
            getLessons();
        } else {
            alert("Error has occured");
        }
    });
}

function createImage(title, description, url) {
    var imageData = JSON.stringify({"type": IMAGE, "title": title, "description": description, "url": url});
    fetch(queryString, {method: "POST", headers: new Headers({ID_TOKEN}), body: imageData}).then((resp) => {
        if (resp.ok){
            getLessons();
        } else {
            alert("Error has occured");
        }
    });
}

function createContent(title, description, content, urls) {
    var contentData = JSON.stringify({"type": CONTENT, "title": title, "description": description, "content": content, "urls": urls.split(", ")});
    fetch(queryString, {method: "POST", headers: new Headers({ID_TOKEN}), body: contentData}).then((resp) => {
        if (resp.ok){
            getLessons();
        } else {
            alert("Error has occured");
        }
    });
}

function joinRoom() {
    return fetch("/join?room_id=" + getRoomId() + "&action=join", {method: "POST", headers: new Headers({ID_TOKEN})}); 
}

function unjoinRoom() {
    return fetch("/join?room_id=" + getRoomId() + "&action=unjoin", {method: "POST", headers: new Headers({ID_TOKEN})});
}

function getRoomId() {
    return new URL(window.location.href).searchParams.get("room_id");
}

function getJoinStatus() {
    var action = new URL(window.location.href).searchParams.get("action");
    if (action == null) {
        return "unjoin";
    }
    return action;
}

function getLessons() {
    fetch(queryString, {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.json()).then((lessonsList) => {
        const lessonElement = document.getElementById("lesson-container");
        lessonElement.innerHTML = "";
        for (lesson of lessonsList) {
            lessonElement.appendChild(createLessonDivElement(lesson));
        };
    });
}

function getModal(lesson) {
    document.getElementById("modal-type").innerHTML = capitalizeFLetter(lesson.entity.propertyMap.title);
    const type = lesson.entity.propertyMap.type;
    const modalElement = document.getElementById("modal-container");
    modalElement.innerHTML = "";
    if (type == FORM) {
        console.log("TODO: Add in future PR");
    } else if (type == IMAGE) {
        console.log("TODO: Add in future PR");
    } else if (type == VIDEO) {
        modalElement.appendChild(createVideoDivElement(lesson));
    } else if (type == CONTENT) {
        console.log("TODO: Add in future PR");
    }
}

function createLessonDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <div class="card border-danger margin margin-left">
            <img class="card-img-top" src="/assets/soundwave.svg" alt="Lesson Card">
            <div class="card-body text-center">
                <h5 class="card-title" id="lesson-title"></h5>
                <div class="card-text small-text" id="lesson-type"></div>
                <div class="card-text small-text" id="lesson-description"></div>
                <div class="small-spacing-bottom"></div>
                <button type="button" id="lesson-modal" class="btn btn-default" data-toggle="modal" data-target="#ModalCenterLessons">Open</button>
            </div>
        </div>
        `, "text/html");
    doc.getElementById("lesson-title").innerText = capitalizeFLetter(lesson.entity.propertyMap.title);
    doc.getElementById("lesson-type").innerText = capitalizeFLetter(lesson.entity.propertyMap.type);
    doc.getElementById("lesson-description").innerText = getDescription(lesson);
    doc.getElementById("lesson-modal").addEventListener("click", function() {
        getModal(lesson);
    });
    return doc.body;
}

function createVideoDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <iframe id="player" type="text/html" frameborder="0" allowfullscreen></iframe>
    `, "text/html");
    doc.getElementById("player").src = videoPath + new URL(lesson.entity.propertyMap.url).searchParams.get("v");
    return doc.body;
}