// Packages
const httpServer = require("http").createServer();
const io = require("socket.io")(httpServer);
const { MongoClient, Db } = require("mongodb");
const { isJson, queryDB, setValue } = require("utils.js")

const mongoURL = "mongodb://localhost:27017/falcun";
const SOCKET_PORT_NUMBER = 3000;

const tables = ["FeaturedServers", "Groups", "Players", "Cosmetics", "Users"];

let db;

/**
 * @param {Db} db
 * @param {String} collName
 * @returns {boolean}
 */
function collExists(db, collName) {
    db.listCollections({name: collName}).next(function (err, collinfo) {
        if (collinfo) {
            return true;
        }
    });
    return false;
}

MongoClient.connect(mongoURL, async (err, db) => { // create the db
    if (err) {
        console.error(err);
        process.exit(0);
    }

    const dbo = db.db("falcun");
    this.db = dbo;

    for (let i = 0; i < tables.length; i++) {
        const j = tables[i];
        if (!collExists(dbo, j)) {
            await dbo.createCollection(j);
        }
    }

    console.log("db init");
    await db.close();
});

httpServer.listen(SOCKET_PORT_NUMBER);

io.on("connection", async (socket) => {
    console.log(`Got connection from client.`);

    // send a message to the client
    socket.emit("connected", "Hello from the server!");

    socket.once("verification", async (args) => {
        if (!isJson(args)) return; // invalid request

        const data = JSON.parse(args);
        if(!data || !data.hwid || !data.email) return; // invalid request

        if(!db) return console.log("Invalid db object!");

        const results = await queryDB(db, "Users", { email: data.email })
        if(!results.length) return; // invalid email

        if(results.hwid === "none") {
            await setValue(db, "Users", { email: data.email }, { $set: { hwid: data.hwid } })
            return socket.emit("verification", "good");
        }

        if(results.hwid !== data.hwid) return;

        socket.emit("verification", "good");
    })

    socket.on("getPlayerCosmetics", async(uuid) => {

    })

});