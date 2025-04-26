import { Avatar, Card, List, message } from "antd";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { fetchData } from "../../utils/fetchData";

function TeamList() {
  const [params] = useSearchParams();
  const [loading, setLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const teamId = params.get("teamId");
  const leagueId = params.get("leagueId");
  const [teamData, setTeamData] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [teamDetails, setTeamDetails] = useState([]);

  const fetchTeamDetails = async () => {
    setLoading(true);
    if (teamData.length === 0) {
      const params = new URLSearchParams({
        leagueId: leagueId,
      });
      await fetchData(
        `team?${params.toString()}`,
        (data) => {
          setTeamData(data);
          processTeamData(data, teamId);
        },
        () => {
          // Handle error
          console.error("Failed to fetch team list");
          messageApi.error(`Error fetching team list. Please try again.`);
        },
        setLoading
      );
    }
  };

  const processTeamData = (data, teamId) => {
    const currentTeam = data.find((team) => team.team_key === teamId);
    if (currentTeam) {
      const formattedData = currentTeam.players.map((player) => ({
        key: player.player_id,
        playerName: player.player_complete_name,
        playerType: player.player_type,
        playerImage: player.player_image,
      }));
      setSelectedTeam(currentTeam);
      setTeamDetails(formattedData);
    }
  };

  useEffect(() => {
    if (teamId) {
      if (teamData.length === 0) {
        fetchTeamDetails();
      }
      if (teamData.length > 0) {
        processTeamData(teamData, teamId);
      }
    }
  }, [teamId]);
  return (
    <>
      {contextHolder}
      {loading && (
        <Card className="w-[425px] bg-white shadow-md">
          <p className="text-gray-500 text-center">Loading team...</p>
        </Card>
      )}
      {selectedTeam && (
        <Card
          title={selectedTeam.team_name}
          className="w-[425px] bg-white shadow-md"
          style={{ padding: "12px" }}
        >
          {contextHolder}
          <List
            itemLayout="horizontal"
            dataSource={teamDetails}
            size="small"
            pagination={{
              position: "bottom",
              align: "end",
              defaultPageSize: 7,
            }}
            renderItem={(item, index) => (
              <List.Item>
                <List.Item.Meta
                  avatar={
                    <Avatar
                      src={
                        item.playerImage !== ""
                          ? item.playerImage
                          : `https://api.dicebear.com/7.x/miniavs/svg?seed=${index}`
                      }
                    />
                  }
                  title={<a href="https://ant.design">{item.playerName}</a>}
                  description={`${item.playerType}`}
                />
              </List.Item>
            )}
          />
        </Card>
      )}
    </>
  );
}

export default TeamList;
