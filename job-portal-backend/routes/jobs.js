const express = require('express');
const router = express.Router();

// Placeholder data for testing the Android UI
const mockJobs = [
    {
        id: '1',
        title: 'Senior Kotlin Developer',
        company: 'TechInnovate Solutions',
        location: 'Remote, CA',
        salaryRange: '$120K - $150K',
        descriptionSnippet: 'Leading the development of new Android/Compose features.',
        postDate: 'Posted 2 hours ago',
        jobType: 'Full-time'
    },
    {
        id: '2',
        title: 'UI/UX Designer (Mobile)',
        company: 'Creative Labs',
        location: 'New York, NY',
        salaryRange: '$80K - $100K',
        descriptionSnippet: 'Design user-centered interfaces for mobile platforms.',
        postDate: 'Posted 1 day ago',
        jobType: 'Contract'
    },
    {
        id: '3',
        title: 'Backend Engineer (Node.js/Mongo)',
        company: 'DataStream Corp',
        location: 'Seattle, WA',
        salaryRange: '$100K - $130K',
        descriptionSnippet: 'Build and maintain our scalable API using Express and MongoDB.',
        postDate: 'Posted 4 days ago',
        jobType: 'Full-time'
    }
];

// --- Endpoint to handle the Job Feed GET request ---
// Full path: /api/jobs/feed
router.get('/feed', async (req, res) => {
    console.log("JOB FEED DEBUG: GET /jobs/feed route hit. Sending mock data.");

    try {
        // Sends the structured response expected by the Android client (JobFeedResponse)
        res.status(200).json({
            success: true,
            jobs: mockJobs, // Send the mock data
            message: 'Job feed loaded successfully.'
        });
    } catch (error) {
        console.error("Job Feed Error (500):", error);
        res.status(500).json({
            success: false,
            jobs: [],
            message: 'Server error while fetching job data.'
        });
    }
});

// Placeholder for job posting
router.post('/post', (req, res) => {
    res.status(501).json({ success: false, message: 'Job posting not implemented.' });
});

module.exports = router;