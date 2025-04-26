import { Card, Col, message, Row, Select, Space, Switch } from "antd";
import { useCallback, useEffect, useState } from "react";
import { useSearchParams } from "react-router";
import { fetchData } from "../../utils/fetchData";

function Selection() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState(
    searchParams.get("countryId") ?? null
  );
  const [selectedLeague, setSelectedLeague] = useState(
    searchParams.get("leagueId") ?? null
  );
  const [countries, setCountries] = useState([]);
  const [leagues, setLeagues] = useState([]);
  const [messageApi, contextHolder] = message.useMessage();
  const [online, setOnline] = useState(true);

  const fetchCountries = useCallback(() => {
    fetchData(
      "countries",
      (data) => {
        const formattedCountries = data.map((country) => ({
          value: country.country_id,
          label: country.country_name,
        }));
        setCountries(formattedCountries);
      },
      () => {
        setCountries([]);
        messageApi.error(`Error fetching countries. Please try again.`);
      },
      setLoading
    );
  }, [fetchData]);

  const fetchLeagues = useCallback(
    (countryId) => {
      const params = new URLSearchParams({
        countryId: countryId,
      });
      fetchData(
        `league?${params.toString()}`,
        (data) => {
          const formattedLeagues = data.map((league) => ({
            value: league.league_id,
            label: league.league_name,
          }));
          setLeagues(formattedLeagues);
        },
        () => {
          setLeagues([]);
          setSelectedLeague(null);
          setSelectedCountry(null);
          messageApi.error(
            `Error fetching leagues for country ID: ${countryId}. Please try again.`
          );
        },
        setLoading
      );
    },
    [fetchData]
  );

  useEffect(() => {
    fetchCountries();
  }, [fetchCountries]);

  useEffect(() => {
    if (!selectedCountry) return;
    fetchLeagues(selectedCountry);
  }, [selectedCountry, fetchLeagues]);

  const handleCountryChange = (value) => {
    setSelectedCountry(value);
    setSelectedLeague(null);
    setSearchParams({ countryId: value });
  };

  const handleLeagueChange = (value) => {
    setSelectedLeague(value);
    const existingParams = Object.fromEntries(searchParams.entries());
    setSearchParams({ ...existingParams, leagueId: value });
  };

  const handleSwitchChange = (value) => {
    setOnline(value);
    const existingParams = Object.fromEntries(searchParams.entries());
    setSearchParams({ ...existingParams, online: value });
  };

  return (
    <Card
      title="Select Country and League"
      className="w-[725px] bg-white shadow-md"
      extra={
        <Switch
          checkedChildren="online"
          unCheckedChildren="offline"
          onChange={handleSwitchChange}
          value={online}
        />
      }
    >
      {contextHolder}
      <Row className="gap-4 items-center">
        {loading && countries.length === 0 && (
          <div className="text-gray-500">Loading countries...</div>
        )}
        {countries.length > 0 && (
          <>
            <Col flex={2} className="flex flex-col gap-2">
              {countries.length > 0 && (
                <>
                  <label className="">Select Country</label>
                  <Select
                    value={selectedCountry}
                    options={countries}
                    placeholder="Select a country"
                    onChange={handleCountryChange}
                  />
                </>
              )}
            </Col>
            <Col flex={2} className="flex flex-col gap-2">
              <label className="">Select League</label>
              <Select
                value={selectedLeague}
                options={leagues}
                placeholder="Select a league"
                onChange={handleLeagueChange}
              />
            </Col>
          </>
        )}
      </Row>
      <Space direction="vertical" className="w-80">
        <Space></Space>
      </Space>
      <Space direction="vertical">
        <Space></Space>
      </Space>
    </Card>
  );
}

export default Selection;
