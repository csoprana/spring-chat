let socket;
let object;
let stompClient;
let sessionId = document.querySelector("#sessionId").textContent;
let messages;

//otteniamo gli ultimi messaggi
//TODO ottenere nomi dalla rubrica (da implementare)
fetch('http://localhost:8080/data?user1=cris&user2=james')
  .then(response => response.json())
  .then(data => {
	messages = data;
	messages.forEach((element) => displayMessages(element));
	
});

function displayMessages(message){
	if(sessionId != message.sender){
		let chat = document.querySelector(".chat__content");
	  	const newDiv = document.createElement("div");
		const newDiv2 = document.createElement("div");
		const newContent = document.createTextNode(message.body);
		newDiv2.appendChild(newContent);
		newDiv2.classList.add("recipient-message");
		newDiv.appendChild(newDiv2);
		newDiv2.setAttribute("data-message-id", message.uuid);
		newDiv.classList.add("recipient-message-container");
		chat.appendChild(newDiv);
	} else {
		let chat = document.querySelector(".chat__content");
	  	const newDiv = document.createElement("div");
		const newDiv2 = document.createElement("div");
		const newContent = document.createTextNode(message.body);
		newDiv2.appendChild(newContent);
		newDiv2.classList.add("sender-message");
		//controlliamo se è stato ricevuto o meno
		if(message.hasReceived == true){
			//doppie spunte
			let i = document.createElement("i");
			i.classList.add("fas", "fa-check", "check-icon");
			newDiv2.appendChild(i);
			i = document.createElement("i");
			i.classList.add("fas", "fa-check", "check-icon");
			newDiv2.appendChild(i);
		}else {
			//singola spunta
			let i = document.createElement("i");
			i.classList.add("fas", "fa-check", "check-icon");
			newDiv2.appendChild(i);
		}
		newDiv.appendChild(newDiv2);
		newDiv2.setAttribute("data-message-id", message.uuid);
		newDiv.classList.add("sender-message-container");
		chat.appendChild(newDiv);
	}
	
	
	
	
	
	
	
	
	
	
}

//generato dal file stomp.js quando arriva un messaggio RECEIPT
document.addEventListener("myEvent", (event) => {
	let chat = document.querySelector(".chat__content");
	let list = chat.querySelectorAll('[data-id]');
	let string;
	for(let i = 0;i<list.length;i++){
		string = "[" + list[i].getAttribute("data-id") + "]";
		if(string == event.detail.receipt_id){
			console.log(list[i].children[0].classList.remove("fa-spinner" ,"loading-icon"));
			list[i].children[0].classList.add("fa-check");
			list[i].setAttribute("data-message-id", event.detail.uuid);
			console.log(event.detail.uuid);
		}
	}
})




// per il momento gli utenti sono hard coded 
//TODO fare rubrica
if(sessionId == "cris"){
	document.querySelectorAll(".recipient-name")[0].textContent = "James Sunderland";
	document.querySelectorAll(".recipient-name")[1].textContent = "James Sunderland";
}else if(sessionId == "james"){
	
	document.querySelectorAll(".recipient-name")[0].textContent = "Cristian Santana";
	document.querySelectorAll(".recipient-name")[1].textContent = "Cristian Santana";
}


// creazione di una connessione
connect();


function connect(){
	socket = new WebSocket("ws://localhost:8080/chat");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, () => {
		stompClient.subscribe("/user/queue/reply", (data) => {
			object = data;
			create_answer(JSON.parse(data.body).body);
			data.ack();
		},{ack: 'client'})
	}, () => console.log("Errore nel subscribe"))
}



function send(uuid){
	// TODO fare rubrica
	let recipient;
	if(sessionId == "cris"){
		recipient = "james";
	}else if(sessionId == "james"){
		recipient = "cris";
	}
	stompClient.send("/app/sendmessage", {"test" : "test", "receipt_id": uuid}, JSON.stringify({'recipient': recipient, 'body': document.querySelector("#input-message").value}));
}



function disconnect(){
	stompClient.disconnect();
}



//TODO accorpare questi metodi
function create_answer(data){
	let chat = document.querySelector(".chat__content");
  	const newDiv = document.createElement("div");
	
	const newDiv2 = document.createElement("div");
	const newContent = document.createTextNode(data);
	newDiv2.appendChild(newContent);
	newDiv2.classList.add("recipient-message");
	newDiv.appendChild(newDiv2);
	newDiv.classList.add("recipient-message-container");
	chat.appendChild(newDiv);
	
}

function create_message(){
	let chat = document.querySelector(".chat__content");
  	const newDiv = document.createElement("div");
	const newDiv2 = document.createElement("div");
	const newContent = document.createTextNode(document.querySelector('#input-message').value);
	newDiv2.appendChild(newContent);
	newDiv2.classList.add("sender-message");
	newDiv.appendChild(newDiv2);
	let uuid = uuidGenerator();
	newDiv2.setAttribute("data-id", uuid);
	const i = document.createElement("i");
	i.classList.add("fas", "fa-spinner", "loading-icon");
	newDiv2.appendChild(i);
	newDiv.classList.add("sender-message-container");
	chat.appendChild(newDiv);
	send(uuid);
	document.querySelector('#input-message').value = "";
}

document.querySelector('#input-message').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      console.log("inviato!");
	create_message();
	
    }
});


//genera un uuid lato client per l'acknowledge dei messaggi inviati
// viene usato solo in fase di invio, poi viene sostituito da uuoid generato da server
function uuidGenerator() {
    var S4 = function() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    };
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}





// Questa parte gestisce l'apertura della barra laterale'
//TODO sistemare bug scorrimento verticale bloccato

let element = document.querySelector(".chat__content");
element.scrollTop = element.scrollHeight - element.clientHeight;
let isOpen = false;
document.querySelector(".chat-header").addEventListener("click", () => {
	console.log(isOpen);
	if(!isOpen){
		document.querySelector(".right-column").classList.toggle("display-block");
	}

	if(document.querySelector(".right-column").classList.contains("display-block")){
		if(isOpen==false){
			setTimeout(() => {
			document.querySelector(".right-column").classList.add("flex");
			
		}, 0.1);
			setTimeout(() => {
			document.querySelector(".close").classList.remove("hide");
			
		}, 500);
		}
		
		
		
		isOpen = true;
		console.log(isOpen);
	}else {
		
		//document.querySelector(".right-column").classList.remove("flex");
	}
		
});


document.querySelector(".close").addEventListener("click", () => {
	console.log(isOpen);
	console.log("premuto!");
	document.querySelector(".close").classList.add("hide");
	document.querySelector(".right-column").classList.remove("flex");
	setTimeout(() => {
		isOpen = false;
		document.querySelector(".right-column").classList.toggle("display-block");
		console.log(isOpen);
	}, 500);
	
})

