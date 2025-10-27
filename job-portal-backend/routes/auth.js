const express = require('express');
const router = express.Router();
const User = require('../models/User');

// --------------------------------------------------
// 1. REGISTRATION ROUTE (/api/auth/register)
// --------------------------------------------------
router.post('/register', async (req, res) => {
    console.log('--- Register Attempt Received ---'); // <-- This is your console notification!
    const { email, name, password, confirmPassword, isRecruiter } = req.body;

    // Quick validation check
    if (password !== confirmPassword) {
         return res.status(400).json({ success: false, message: "Passwords do not match." });
    }

    try {
        const role = isRecruiter ? 'recruiter' : 'job_seeker';

        // Check for existing user
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            console.log(`Conflict: Email ${email} already in use.`); // Notification on conflict
            return res.status(409).json({
                success: false,
                message: "Registration failed: This email address is already in use."
            });
        }

        // Save new user
        const newUser = new User({ email, name, password, role });
        await newUser.save();

        console.log(`Success: New user registered: ${email}`); // Notification on success

        // Success response
        res.status(201).json({
            success: true,
            message: "Registration successful",
            userId: newUser._id,
            userEmail: newUser.email
        });

    } catch (err) {
        console.error("CRITICAL SIGN UP ERROR (500):", err);
        if (err.code === 11000) {
            return res.status(409).json({
                success: false,
                message: "Registration failed: This email address is already in use."
            });
        }
        res.status(500).json({ success: false, message: "A server error occurred." });
    }
});


// --------------------------------------------------
// 2. LOGIN ROUTE (/api/auth/login)
// --------------------------------------------------
router.post('/login', async (req, res) => {
    console.log('--- Login Attempt Received ---'); // <-- This is your console notification!
    const { email, password } = req.body;

    try {
        // 1. Find the user by email
        const user = await User.findOne({ email });
        if (!user) {
            console.log(`Login Failed: User ${email} not found.`); // Notification on failure
            return res.status(401).json({ success: false, message: "Invalid credentials." });
        }

        // 2. Validate the password (Placeholder comparison)
        const isMatch = (password === user.password);

        if (!isMatch) {
            console.log(`Login Failed: Invalid password for ${email}.`);
            return res.status(401).json({ success: false, message: "Invalid credentials." });
        }

        console.log(`Success: User logged in: ${email}`); // Notification on success

        // 3. Success: Return user ID, email, type, and token
        res.status(200).json({
            success: true,
            token: "EXAMPLE_JWT_TOKEN",
            userId: user._id,
            userType: user.role,
            message: "Login successful"
        });

    } catch (err) {
        console.error("LOGIN ERROR:", err);
        res.status(500).json({ success: false, message: "A server error occurred during login." });
    }
});

module.exports = router;