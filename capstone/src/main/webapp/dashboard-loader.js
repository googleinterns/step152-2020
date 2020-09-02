document.getElementById("signout").prepend(dynamicButton);
window.addEventListener('authorized', getRooms);

const RECOMMENDED_TYPE_NAME = "recommended";
const FOLLOWED_TYPE_NAME = "followed";

function createRoom(title, description) {
    var roomData = JSON.stringify({"title": title, "description": description});
    fetch("/dashboard", {method: "POST", headers: new Headers({ID_TOKEN}), body: roomData}).then((resp) => {
        if (resp.ok){
            getRooms();
        } else {
            console.log("Error has occured");
        }
    });
}

function getRooms(type='') {
    fetch("/dashboard", {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.json()).then((roomsList) => {
        if (type == RECOMMENDED_TYPE_NAME) {
            roomsList = roomsList[2];
        } else if (type == FOLLOWED_TYPE_NAME) {
            roomsList = roomsList[1];
        } else {
            roomsList = roomsList[0];
        }
        const roomElement = document.getElementById("room-container");
        roomElement.innerHTML = "";
        for (room of roomsList) {
            roomElement.appendChild(createRoomDivElement(room));
        };
    });
}

// TODO: Update search in future PR
function getSearchResults(type='') {
   fetch("/search?search=" + document.getElementById('search').value, {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.json()).then((roomList) => {
        const roomElement = document.getElementById("room-container");
        roomElement.innerHTML = "";
        for (room of roomList) {
            roomElement.appendChild(createRoomDivElement(room));
        };
    }); 
}

function followerCount(room) {
    var count = 0
    if (room.entity.propertyMap.followers == null) {
        return "Follower: " + count;
    }
    return "Follower: " + room.entity.propertyMap.followers.length;
}

function createRoomDivElement(room) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
            <div class="card margin margin-left">
                <img class="card-img-top" src="/assets/soundwave.svg" alt="Room Card">
                <div class="card-body text-center">
                    <h5 class="card-title" id="room-title"></h5>
                    <div class="card-text small-text" id="room-followers"></div>
                    <div class="card-text small-text" id="room-description"></div>
                    <div class="card-text small-text">Tag(s): </div>
                    <div class="small-spacing-bottom"></div>
                    <div class="btn btn-default" id="room-link">Go</div>
                    <button type="button" id="room-delete" class="btn btn-default" data-toggle="modal" data-target="#ModalCenterRoomDelete">Delete</button>
                </div>
            </div>
            `, "text/html");
    doc.getElementById("room-title").innerText = capitalizeFLetter(room.entity.propertyMap.title);
    doc.getElementById("room-description").innerText = getDescription(room);
    doc.getElementById("room-followers").innerText = followerCount(room);
    doc.getElementById("room-link").addEventListener("click", function() {
        window.location.href = "lesson.html?room_id=" + room.entity.key.id;
    });
    document.getElementById("room-delete-button").addEventListener("click", function() {
        deleteFromDatastore(room);
    });
    return doc.body;
}