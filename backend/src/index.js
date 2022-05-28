(async () => {
  const express = require('express');
  const app = express();
  const bodyParser = require('body-parser');
  const cors = require('cors');
  const mongoose = require('mongoose');
  const http = require('http');
  const Clients = require('./schemas/example.js');
  const server = http.createServer(app).listen(3000);
  const engine = require('socket.io')(server, {
    aallowEIO3: true,
  });
  app.use(bodyParser.json());
  app.use(
    bodyParser.urlencoded({
      extended: true,
    })
  );
  app.use(require('cors')());

  const db = await mongoose.connect('mongodb://localhost:27017/falcunclient', {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  });

  const Websocket = new (require('./libs/Websocket.js'))({
    engine: engine,
    db: db,
  });

  // ** example mongoose ** //
  const client = new Clients({
    hwid: '123456789',
  }).save(async err => {
    if (err) throw err;
    console.log('Client saved successfully!');
  });

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
})();
