const { MongoClient, Db } = require("mongodb");

function isJson(item) {
    item = typeof item !== "string"
        ? JSON.stringify(item)
        : item;

    try {
        item = JSON.parse(item);
    } catch (e) {
        return false;
    }

    return typeof item === "object" && item !== null;
}

/**
 * @param {Db} db
 * @param {String} collectionName
 * @param queryStr
 */
function queryDB(db, collectionName, queryStr) {
    return db.collection(collectionName).find(queryStr).toArray(function(err, result) {
        if (err) throw err;
        return console.log(result);
    });
}

/**
 * @param {Db} db
 * @param {String} collectionName
 * @param query
 * @param { {$set} } newValues
 */
function setValue(db, collectionName, query, newValues) {
    return db.collection(collectionName).updateOne(query, newValues, function(err, res) {
        return !err;
    });
}

module.exports = {
    isJson: isJson,
    queryDB: queryDB,
    setValue: setValue
}