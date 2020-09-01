document.getElementById("signout").prepend(dynamicButton);
window.addEventListener('authorized', getRooms);

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

function getRooms() {
    fetch("/dashboard", {method: "GET", headers: new Headers({ID_TOKEN})}).then(response => response.json()).then((roomsList) => {
        const roomElement = document.getElementById("room-container");
        roomElement.innerHTML = "";
        for (room of roomsList) {
            roomElement.appendChild(createRoomDivElement(room));
        };
    });
}

function getSearchResults() {
    fetch("/search?search=" + document.getElementById('search').value, {method: "GET", headers: new Headers({ID_TOKEN})})
        .then(response => response.json())
        .then((roomList) => {
            const roomElement = document.getElementById("room-container");
            roomElement.innerHTML = "";
            for (room of roomList) {
                roomElement.appendChild(createRoomDivElement(room));
            };
    }); 
}

function createRoomDivElement(room) {
    let domparser = new DOMParser();
    let doc = domparser.parseFromString(`
            <div class="card margin margin-left">
                <img class="card-img-top" src="/assets/soundwave.svg" alt="Room Card">
                <div class="card-body text-center">
                    <h5 class="card-title" id="room-title"></h5>
                    <a class="card-text small-text" href="#">Kyle Smith</a>
                    <div class="card-text small-text">Followers: Infinite</div>
                    <div class="card-text small-text">Tag(s): </div>
                    <div class="small-spacing-bottom"></div>
                    <div class="btn btn-default" id="room-link"></button>
                </div>
            </div>
            `, "text/html");
    
    doc.getElementById("room-title").innerText = room.entity.propertyMap.title;
    doc.getElementById("room-link").addEventListener("click", function() {
        window.location.href = "lesson.html?room_id=" + room.entity.key.id;
    });
    doc.getElementById("room-link").innerText = "View";
    return doc.body;
}