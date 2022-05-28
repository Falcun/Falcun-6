// create a simple socket io client with nodejs
const io = require('socket.io-client');

const socket = io("http://localhost:3000");

socket.on("connect", () => {
    console.log("connected");
});

socket.on("disconnect", () => {
    console.log("disconnected");
});

socket.on("connected", (args) => {
    console.log(args);

    socket.emit("verification", JSON.stringify({ email:"2243@fma.com", hwid:"1223243ujw28w890dfr" }));
})

socket.on("message", (data) => {
    console.log(data);
});

socket.on("error", (err) => {
    console.error(err);
});

socket.on("connect_error", (err) => {
    console.error(err);
});