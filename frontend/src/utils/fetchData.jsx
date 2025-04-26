export const fetchData = async (url, onSuccess, onError, setLoading = null) => {
  const apiUrl = import.meta.env.VITE_API_URL;
  if (setLoading) setLoading(true);
  try {
    const response = await fetch(apiUrl + url);
    if (response.status !== 200) {
      throw new Error(`Failed to fetch data from ${url}`);
    }
    const data = await response.json();
    onSuccess(data);
  } catch (error) {
    console.error("Error fetching data:", error);
    if (onError) onError(error);
  } finally {
    if (setLoading) setLoading(false);
  }
};
