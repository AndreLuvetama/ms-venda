import UserRepository from "../repository/UserRepository.js";
import * as httpStatus from "../../../config/constants/httpStatus.js";
import * as secrets from "../../../config/constants/secrets.js";
import UserException from "../exception/UserException.js";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

class UserService{
    async findByEmail(req){
        try{
            const{ email } = req.params;
            const{ authUser } = req;
            this.validateRequestData(email);
            let user = await UserRepository.findByEmail(email);           
            this.validateUserNotFound(user);
            this.validateAuthenticatedUser(user, authUser);
            console.log("Validando a authUser", authUser);            
            return{
                status: httpStatus.SUCCESS,
                user:{
                    id: user.id,
                    name: user.name,
                    email: user.email,
                },
            };
        }catch(err){
            return{
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };
        }
    }

    validateRequestData(email){
        if(!email){            
            throw new UserException(httpStatus.BAD_REQUEST,
                 "User email was not informed.");
        }
    }
    validateUserNotFound(user){
        //console.log("Validando email", email);
        if(!user){
            throw new UserException(httpStatus.BAD_REQUEST, "User not found.");
        }
    }

    
    async getAccessToken(req){
        try{
            const { email, password } = req.body; // Estão no Body        
            this.validateAccessTokenData(email, password);                                                                                                                                                                   
            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user);
            await this.validatePassword(password, user.password);
            const authUser = {id: user.id, name: user.name, email: user.email};
            const accessToken = jwt.sign({authUser}, secrets.API_SECRET, {expiresIn: "1d",});
            return{
                status: httpStatus.SUCCESS,
                accessToken, 
            };
        } catch (err){
            return{
                status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: err.message,
            };     
        }
    }

    validateAuthenticatedUser(user, authUser){
        console.log("Validando a authUser 1", user.id);  
        if(!authUser || (user.id !== authUser.id)){
            throw new UserException(
                httpStatus.FORBIDDEN, "You cannot see user data."
            );
        }
    }

    validateAccessTokenData(email, password){
        if(!email || !password){
            throw new UserException(httpStatus.ANAUTHORIZED, "Email and password must be informed");
        }
    }    
    async validatePassword(password, hashPassword){
        if(!await bcrypt.compare(password, hashPassword)){
            throw new UserException(httpStatus.ANAUTHORIZED, "Password doens't match.");    
        }    
    }
}


export default new UserService();