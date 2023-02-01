import Sequelize from "sequelize";

const sequelize = new Sequelize("auth-db", "postgres", "sa123",{
    host: "localhost",
    dialect: "postgres",
    quoteIdentifiers: false,
    define:{
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true // mantém o nome da tab conforme foi definido
    },
}); 

sequelize.authenticate()
.then(() => {
console.info("Connection has been stabilished!");
})
.catch((err) => {
    console.error("Unable to connerct to the database");
    console.error(err.message);
});

export default sequelize;
