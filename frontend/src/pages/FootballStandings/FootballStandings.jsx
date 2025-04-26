import { Space } from "antd";
import Selection from "./Selection";
import LeagueStandings from "./LeagueStandings";
import TeamList from "./TeamList";

function FootballStandings() {
  return (
    <>
      <Space className="mt-3 pt-3 w-full flex justify-center">
        <Space direction="vertical" className="w-full flex flex-col">
          <Selection />
          <LeagueStandings />
        </Space>
        <Space direction="vertical" className="w-full flex flex-col">
          <TeamList />
        </Space>
      </Space>
    </>
  );
}

export default FootballStandings;
