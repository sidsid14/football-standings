import { Button, Card, message, Table } from "antd";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { fetchData } from "../../utils/fetchData";

function LeagueStandings() {
  const [searchParams, setSearchParams] = useSearchParams();
  const leagueId = searchParams.get("leagueId");
  const selectedTeam = searchParams.get("teamId");
  const online =
    searchParams.get("online") === null
      ? true
      : searchParams.get("online") === "true";
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const columns = [
    {
      title: "Overall League Position",
      dataIndex: "overall_league_position",
      key: "overall_league_position",
    },
    {
      title: "Team Name",
      dataIndex: "team_name",
      key: "team_name",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Button
          color={selectedTeam === record.key ? "primary" : "default"}
          variant="outlined"
          shape="round"
          onClick={() => handleAction(record)}
        >
          View Team
        </Button>
      ),
    },
  ];

  const handleAction = (record) => {
    const teamId = record.key;
    const existingParams = Object.fromEntries([...searchParams]);
    const newParams = {
      ...existingParams,
      teamId: teamId,
    };
    setSearchParams(newParams);
  };
  const [dataSource, setDataSource] = useState([]);
  const fetchLeagueStandings = async () => {
    setLoading(true);
    const params = new URLSearchParams({
      leagueId: leagueId,
      offlineMode: !online,
    });
    fetchData(
      `standings?${params.toString()}`,
      (data) => {
        // Handle successful data fetch
        const formattedData = data._embedded.linkedHashMapList.map((team) => ({
          key: team.team_id,
          overall_league_position: team.overall_league_position,
          team_name: team.team_name,
        }));
        setDataSource(formattedData);
      },
      () => {
        // Handle error
        console.error("Failed to fetch league standings");
        messageApi.error(`Error fetching league standings. Please try again.`);
      },
      setLoading
    );
  };
  useEffect(() => {
    if (leagueId) {
      fetchLeagueStandings();
    }
  }, [leagueId, online]);

  return (
    <Card
      title="League Standings"
      className="w-[725px] bg-white shadow-md"
      extra={<Button onClick={fetchLeagueStandings}>Refresh</Button>}
    >
      {contextHolder}
      {loading && <p className="text-gray-500">Loading standings...</p>}
      {leagueId ? (
        !loading && (
          <Table
            dataSource={dataSource}
            columns={columns}
            size="small"
            pagination={{ defaultPageSize: 5 }}
          />
        )
      ) : (
        <p className="text-gray-500">
          Please select a league to view standings.
        </p>
      )}
    </Card>
  );
}

export default LeagueStandings;
