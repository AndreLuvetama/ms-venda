import Sequelize from "sequelize";
import sequelize from "../../../config/db/dbconfig.js";

//Definição dos dados para criação da tab user no BD 'tab user'
const User = sequelize.define("user", {
    id:{
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
