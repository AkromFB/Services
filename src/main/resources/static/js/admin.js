document.addEventListener("DOMContentLoaded", () => {
    const host = `${window.location.protocol}//${window.location.host}`;
    const ctx = document.getElementById("usersChart").getContext("2d");

    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");
    const groupBySelect = document.getElementById("groupBy");

    let chartInstance = null;
    let allUsers = [];

    function formatKey(dateValue, groupBy) {
        const date = new Date(dateValue);
        if (Number.isNaN(date.getTime())) return null;

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");

        if (groupBy === "year") return `${year}`;
        if (groupBy === "month") return `${year}-${month}`;
        return `${year}-${month}-${day}`;
    }

    function filterUsers(users, startDate, endDate) {
        const start = startDate ? new Date(startDate) : null;
        const end = endDate ? new Date(endDate) : null;

        if (end) end.setHours(23, 59, 59, 999);

        return users.filter(user => {
            const createdAt = new Date(user.createdAt);
            if (Number.isNaN(createdAt.getTime())) return false;
            if (start && createdAt < start) return false;
            if (end && createdAt > end) return false;
            return true;
        });
    }

    function groupUsers(users, groupBy) {
        const counts = new Map();

        users.forEach(user => {
            const key = formatKey(user.createdAt, groupBy);
            if (!key) return;
            counts.set(key, (counts.get(key) || 0) + 1);
        });

        const labels = Array.from(counts.keys()).sort();
        const values = labels.map(label => counts.get(label));

        return { labels, values };
    }

    function renderChart() {
        const filteredUsers = filterUsers(
            allUsers,
            startDateInput.value,
            endDateInput.value
        );

        const { labels, values } = groupUsers(filteredUsers, groupBySelect.value);

        if (chartInstance) {
            chartInstance.destroy();
        }

        chartInstance = new Chart(ctx, {
            type: "bar",
            data: {
                labels,
                datasets: [{
                    label: "Utenti registrati",
                    data: values,
                    backgroundColor: "rgba(2, 175, 74, 0.35)",
                    borderColor: "#02af4a",
                    borderWidth: 1,
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        labels: {
                            color: "#ffffff"
                        }
                    }
                },
                scales: {
                    x: {
                        ticks: {
                            color: "#b3b3b3"
                        },
                        grid: {
                            color: "rgba(255,255,255,0.06)"
                        }
                    },
                    y: {
                        beginAtZero: true,
                        ticks: {
                            color: "#b3b3b3",
                            stepSize: 1
                        },
                        grid: {
                            color: "rgba(255,255,255,0.06)"
                        }
                    }
                }
            }
        });
    }

    async function loadUsersAndBuildChart() {
        try {
            const tokenResponse = await fetch(`${host}/admin/session-token`);
            if (!tokenResponse.ok) {
                throw new Error(`Errore token: ${tokenResponse.status}`);
            }

            const tokenData = await tokenResponse.json();
            const adminApiToken = tokenData.token;

            const response = await fetch(`${host}/admin/api/users`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${adminApiToken}`,
                    Accept: "application/json"
                }
            });

            if (!response.ok) {
                throw new Error(`Errore nel recupero utenti: ${response.status}`);
            }

            allUsers = await response.json();
            renderChart();
        } catch (error) {
            console.error(error);
        }
    }

    [startDateInput, endDateInput, groupBySelect].forEach(el => {
        el.addEventListener("change", renderChart);
    });

    loadUsersAndBuildChart();
});