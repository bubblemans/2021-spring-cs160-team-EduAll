const mongoose = require('mongoose');

const { updateRecentMessage } = require('./redis');

const url = 'mongodb://localhost:27017/chat';
const messageSchema = new mongoose.Schema({
  message_id: String,
  sender: String,
  room_id: String,
  message: String,
  data: Buffer,
  created_at: Date
});
mongoose.connect(url, {useNewUrlParser: true, useUnifiedTopology: true});
const db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));

const Message = mongoose.model('messages', messageSchema);

async function queryMessages() {
  const messages = await Message.find();
  return messages;
}

async function insertMessage(message) {
  if (message.created_at === undefined) {
    message.created_at = new Date().toISOString();
  }
  updateRecentMessage(message);
  await Message.create(message);
}

module.exports = {
    queryMessages,
    insertMessage
}