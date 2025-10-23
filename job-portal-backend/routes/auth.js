const router = require('express').Router();
const bcrypt = require('bcryptjs'); // Assuming bcryptjs is used
const User = require('../models/User'); // Mongoose User Model

// --- Sign Up Route ---
router.post('/signup', async (req, res) => {
    // Destructure ALL fields, including name and confirmation password
    const { email, name, password, confirmPassword, isRecruiter } = req.body;

    console.log('Debug: Received Sign Up Request Body:', req.body);

    // Validate for ALL required fields
    if (!email || !name || !password || !confirmPassword) {
        return res.status(400).json({ success: false, message: 'Name, email, password, and confirmation password fields are required.' });
    }

    // Check if passwords match
    if (password !== confirmPassword) {
        return res.status(400).json({ success: false, message: 'Passwords do not match.' });
    }

    try {
        // 1. Check for existing user
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(409).json({ success: false, message: 'User with this email already exists.' });
        }

        // 2. Hash Password
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        // 3. Create and Save New User
        const newUser = new User({
            // FIX: Explicitly map the 'name' property to ensure Mongoose validation passes
            name: name,
            email: email,
            password: hashedPassword,
            role: isRecruiter ? 'recruiter' : 'jobseeker',
        });

        const savedUser = await newUser.save();

        // 4. Respond with success (201 Created)
        res.status(201).json({
            success: true, // Client app expects this
            message: 'User registered successfully',
            user: {
                name: savedUser.name,
                email: savedUser.email,
                role: savedUser.role
            }
        });

    } catch (e) {
        console.error('CRITICAL SIGN UP ERROR (500):', e);
        // If MongoDB validation (like min length) fails, it often triggers this catch block.
        res.status(500).json({ success: false, message: 'Internal server error during sign up.', error: e.message });
    }
});

// --- Login Route ---
router.post('/login', async (req, res) => {
    // Logic for login... (Included for completeness)
    res.status(501).json({ success: false, message: "Login endpoint not implemented yet." });
});

module.exports = router;