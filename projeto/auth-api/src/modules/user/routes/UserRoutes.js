import { Router } from "express";
import UserController from "../controller/UserController.js";
import checkTocken from "../../../config/auth/checkToken.js";

const router = new Router();

router.post("/api/user/auth/", UserController.getAccessToken);
router.use(checkTocken); // Use informa que vai usar uma fonção do midle
router.get("/api/user/email/:email", UserController.findByEmail);

export default router;
