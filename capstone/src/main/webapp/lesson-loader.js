document.getElementById("signout").prepend(dynamicButton);
window.addEventListener('authorized', getLessons);

const FORM = "form";
const VIDEO = "video";
const CONTENT = "content";
const IMAGE = "image";

const FOLLOW = "follow";
const UNFOLLOW = "unfollow";

const queryString = "/lesson?room_id=" + getRoomId();
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

function getUserStatus() {
    fetch("/join?room_id=" + getRoomId(), {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.text()).then((status) => {
        var followButton = document.getElementById("follow-button");
        var createButton = document.getElementById("create-button-lesson");
        if (status.length > 1) {
            followButton.style.display = "block";
            followButton.innerHTML = status;
        } else {
            createButton.style.display = "block";
        }
    });
}

function toggleFollowStatus() {
    status = document.getElementById("follow-button").innerHTML;
    if (status == "" || status == "Unfollow") {
        status = UNFOLLOW;
        document.getElementById('follow-button').innerHTML = capitalizeFLetter(FOLLOW);
    } else {
        status = FOLLOW;
        document.getElementById('follow-button').innerHTML = capitalizeFLetter(UNFOLLOW);
    }
    fetch("/join?room_id=" + getRoomId() + "&action=" + status, {method: "POST", headers: new Headers({ID_TOKEN})});
}

function getRoomId() {
    return new URL(window.location.href).searchParams.get("room_id");
}

function createHyperLink(url) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <a id="object-url"></a>
    `, "text/html");
    doc.getElementById("object-url").href = url;
    doc.getElementById("object-url").innerHTML = url;
    return doc.body;
}

function getRadioOption() {
    var radio = document.getElementById("select-button");
    radio.setAttribute("data-target", document.querySelector('input[name="radio-content"]:checked').value);
}

function getLessons() {
    getUserStatus();
    fetch(queryString, {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.json()).then((lessonsList) => {
        const lessonElement = document.getElementById("lesson-container");
        lessonElement.innerHTML = "";
        for (lesson of lessonsList) {
            lessonElement.appendChild(createLessonCardDivElement(lesson));
        };
    });
}

function getModal(lesson) {
    document.getElementById("modal-type").innerHTML = capitalizeFLetter(lesson.entity.propertyMap.title);
    const type = lesson.entity.propertyMap.type;
    const modalElement = document.getElementById("modal-container");
    modalElement.innerHTML = "";
    if (type == FORM) {
        modalElement.appendChild(createFormDivElement(lesson));
    } else if (type == IMAGE) {
        modalElement.appendChild(createImageDivElement(lesson));
    } else if (type == VIDEO) {
        modalElement.appendChild(createVideoDivElement(lesson));
    } else if (type == CONTENT) {
        modalElement.appendChild(createContentDivElement(lesson));
    }
}

function createLessonCardDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <div class="card margin margin-left">
            <img class="card-img-top" src="/assets/soundwave.svg" alt="Lesson Card">
            <div class="card-body text-center">
                <h5 class="card-title" id="lesson-title"></h5>
                <div class="card-text small-text" id="lesson-type"></div>
                <div class="card-text small-text" id="lesson-description"></div>
                <div class="small-spacing-bottom"></div>
                <button type="button" id="lesson-modal" class="btn btn-default" data-toggle="modal" data-target="#ModalCenterLessons">View</button>
                <button type="button" id="lesson-delete" class="btn btn-default">Delete</button>
            </div>
        </div>
        `, "text/html");
    doc.getElementById("lesson-title").innerText = capitalizeFLetter(lesson.entity.propertyMap.title);
    doc.getElementById("lesson-type").innerText = capitalizeFLetter(lesson.entity.propertyMap.type);
    doc.getElementById("lesson-description").innerText = getDescription(lesson);
    doc.getElementById("lesson-modal").addEventListener("click", function() {
        getModal(lesson);
    });
    doc.getElementById("lesson-delete").addEventListener("click", function() {
        deleteFromDatastore(lesson);
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

function createContentDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <div id="modal-content"></div>
        <div class="small-spacing-bottom"></div>
        <div id="modal-title"><span>Link(s)</span></div>
        <p><div id="modal-urls"></div></p>
    `, "text/html");
    doc.getElementById("modal-content").innerHTML = capitalizeFLetter(lesson.entity.propertyMap.content);
    for (url of lesson.entity.propertyMap.urls) {
        doc.getElementById("modal-urls").appendChild(createHyperLink(url));
    }
    return doc.body;
}

function createFormDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <div id="modal-form-description"></div>
        <div id="modal-title"><span>Form</span></div>
        <a id="modal-form-url"></a
    `, "text/html");
    doc.getElementById("modal-form-description").innerHTML = capitalizeFLetter(lesson.entity.propertyMap.description);
    doc.getElementById("modal-form-url").href = lesson.entity.propertyMap.url;
    doc.getElementById("modal-form-url").innerHTML = "Click Here";
    return doc.body;
}

function createImageDivElement(lesson) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
        <div id="modal-image-description"></div>
        <div class="small-spacing-bottom"></div>
        <div id="modal-title"><span>Image</span></div>
        <img class="img-responsive" id="image-container"></img>
    `, "text/html");
    doc.getElementById("modal-image-description").innerHTML = capitalizeFLetter(lesson.entity.propertyMap.description);
    doc.getElementById("image-container").src = lesson.entity.propertyMap.url;
    return doc.body;
}