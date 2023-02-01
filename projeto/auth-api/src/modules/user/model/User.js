import Sequelize from "sequelize";
import sequelize from "../../../config/db/dbconfig.js";

const User = sequelize.define("user", {
    userid:{
        type: Sequelize.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    name:{
        type: Sequelize.STRING,
        allowNull: false,
    }, 
    email:{
        type: Sequelize.STRING,
        allowNull: false,
    }, 
    password:{
        type: Sequelize.STRING,
        allowNull: false,
    }      
 },   
 {}
);

export default User;    
