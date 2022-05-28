const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');
const http = require('http');
const server = http.createServer(app).listen(3000);
const engine = require('socket.io')(server, {
  aallowEIO3: true,
});
const Websocket = new (require('./libs/Websocket.js'))({
  engine: engine,
});

app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);
app.use(require('cors')());

const sockets = [];

engine.on('connection', async client => {
  await Websocket.setConnections(sockets);
  engine.on('event', async event => {
    await Websocket.onEvent(event);
  });
  client.on('disconnect', async => {
    const socket = sockets.find(socket => socket.socketId === client.id);
    if (socket) sockets.splice(sockets.indexOf(socket), 1);
  });
});
