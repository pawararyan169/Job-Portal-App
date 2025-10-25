const router = require('express').Router();
const bcrypt = require('bcryptjs');
const User = require('../models/User');

// --- Sign Up Route (No changes) ---
router.post('/signup', async (req, res) => {
    const { email, name, password, confirmPassword, isRecruiter } = req.body;

    if (!email || !name || !password || !confirmPassword || password !== confirmPassword) {
        return res.status(400).json({ success: false, message: 'Invalid or incomplete registration data.' });
    }

    try {
        const existingUser = await User.findOne({ email: email.toLowerCase() });
        if (existingUser) {
            return res.status(409).json({ success: false, message: 'User with this email already exists.' });
        }

        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        const newUser = new User({
            name: name,
            email: email,
            password: hashedPassword,
            role: isRecruiter ? 'recruiter' : 'jobseeker',
        });

        await newUser.save();

        res.status(201).json({
            success: true,
            message: 'Registration successful. Please log in.',
            user: { name: newUser.name, email: newUser.email, role: newUser.role }
        });

    } catch (e) {
        console.error('CRITICAL SIGN UP ERROR (500):', e);
        res.status(500).json({ success: false, message: 'Internal server error during sign up.', error: e.message });
    }
});

// --- Login Route (FINAL ROBUST LOGIC) ---
router.post('/login', async (req, res) => {
    console.log("LOGIN ATTEMPT DEBUG: Processing incoming request for:", req.body.email);

    const { email, password } = req.body;

    if (!email || !password) {
        return res.status(400).json({ success: false, message: 'Email and password are required for login.' });
    }

    try {
        const lowerCaseEmail = email.toLowerCase();
        const user = await User.findOne({ email: lowerCaseEmail });

        if (!user) {
            return res.status(401).json({ success: false, message: 'Login failed: Invalid credentials.' });
        }

        // Safety Check: Ensure password hash exists
        if (!user.password) {
             console.error(`ERROR: User ${user.email} found but has no password hash!`);
             return res.status(401).json({ success: false, message: 'Login failed: Account setup error.' });
        }

        // 3. Compare the provided password with the stored hash
        const isMatch = await bcrypt.compare(password, user.password);

        if (!isMatch) {
            return res.status(401).json({ success: false, message: 'Login failed: Invalid credentials.' });
        }

        // Successful login response (HTTP 200 OK)
        console.log(`LOGIN SUCCESS: User ${user.email} authenticated successfully.`);
        res.status(200).json({
            success: true,
            message: 'Login successful',
            userId: user._id.toString(),
            userType: user.role,
        });

    } catch (e) {
        console.error('CRITICAL LOGIN ERROR (500) IN CATCH BLOCK:', e);
        res.status(500).json({ success: false, message: 'Internal server error during login.', error: e.message });
    }
});

module.exports = router;