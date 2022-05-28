const mongoose = require('mongoose'),
  clients = new mongoose.Schema({
    hwid: String,
  });
module.exports = mongoose.model('clients', clients);
