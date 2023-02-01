import express from "express";
import * as db from "./src/config/db/initialData.js";
import userRoutes from "./src/modules/user/routes/UserRoutes.js";
import checkTocken from "./src/config/auth/checkToken.js";

const app = express();
const env = process.env;
const PORT = env.PORT || 8083;

db.createInitialData();
app.use(express.json());
app.use(userRoutes);
app.use(checkTocken);

app.get("/api/status", (req,res) => {
    return res
    .status(200).json({
        service: "Auth-API",
        status: "up",
        httpStatus: 200,
    })
    
}) 

app.listen(PORT, () => {
    console.info(`Server started sucessfully at port ${PORT}`);
});